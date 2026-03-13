package com.aiassistant.common.cache;

import com.aiassistant.common.util.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 多级缓存实现
 * L1: Caffeine本地缓存
 * L2: Redis分布式缓存
 */
@Slf4j
@Component
public class MultiLevelCacheImpl implements MultiLevelCache {
    
    /**
     * L1本地缓存（Caffeine）
     */
    private final com.github.benmanes.caffeine.cache.Cache<String, String> localCache;
    
    /**
     * L2分布式缓存（Redis）
     */
    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;
    
    public MultiLevelCacheImpl() {
        // 初始化Caffeine本地缓存
        this.localCache = com.github.benmanes.caffeine.cache.Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }
    
    @Override
    public <T> T get(String key, Class<T> type) {
        // 1. 先从L1本地缓存获取
        String value = localCache.getIfPresent(key);
        if (value != null) {
            log.debug("L1缓存命中: key={}", key);
            return JacksonUtils.fromJson(value, type);
        }
        
        // 2. L1未命中，从L2 Redis获取
        if (redisTemplate != null) {
            try {
                value = redisTemplate.opsForValue().get(key);
                if (value != null) {
                    log.debug("L2缓存命中: key={}", key);
                    // 回写L1缓存
                    localCache.put(key, value);
                    return JacksonUtils.fromJson(value, type);
                }
            } catch (Exception e) {
                log.error("Redis获取缓存失败: key={}", key, e);
            }
        }
        
        log.debug("缓存未命中: key={}", key);
        return null;
    }
    
    @Override
    public void put(String key, Object value, long ttlSeconds) {
        if (value == null) {
            return;
        }
        
        String jsonValue = JacksonUtils.toJson(value);
        
        // 1. 写入L1本地缓存
        localCache.put(key, jsonValue);
        log.debug("写入L1缓存: key={}", key);
        
        // 2. 写入L2 Redis缓存
        if (redisTemplate != null) {
            try {
                redisTemplate.opsForValue().set(key, jsonValue, ttlSeconds, TimeUnit.SECONDS);
                log.debug("写入L2缓存: key={}, ttl={}s", key, ttlSeconds);
            } catch (Exception e) {
                log.error("Redis写入缓存失败: key={}", key, e);
            }
        }
    }
    
    @Override
    public void evict(String key) {
        // 1. 从L1本地缓存移除
        localCache.invalidate(key);
        log.debug("移除L1缓存: key={}", key);
        
        // 2. 从L2 Redis移除
        if (redisTemplate != null) {
            try {
                redisTemplate.delete(key);
                log.debug("移除L2缓存: key={}", key);
            } catch (Exception e) {
                log.error("Redis移除缓存失败: key={}", key, e);
            }
        }
    }
    
    @Override
    public void clear() {
        // 清空L1本地缓存
        localCache.invalidateAll();
        log.debug("清空L1缓存");
        
        // 注意：不清空L2 Redis缓存，因为可能影响其他实例
    }
    
    @Override
    public boolean exists(String key) {
        // 检查L1
        if (localCache.getIfPresent(key) != null) {
            return true;
        }
        
        // 检查L2
        if (redisTemplate != null) {
            try {
                Boolean hasKey = redisTemplate.hasKey(key);
                return Boolean.TRUE.equals(hasKey);
            } catch (Exception e) {
                log.error("Redis检查缓存存在失败: key={}", key, e);
            }
        }
        
        return false;
    }
}
