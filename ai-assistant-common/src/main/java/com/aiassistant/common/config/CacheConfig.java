package com.aiassistant.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 缓存配置类
 */
@Configuration
@EnableCaching
public class CacheConfig {
    
    /**
     * 配置Caffeine缓存管理器
     * 用于L1本地缓存
     * @return CaffeineCacheManager
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 初始缓存大小
                .initialCapacity(100)
                // 最大缓存数量
                .maximumSize(1000)
                // 写入后过期时间（5分钟）
                .expireAfterWrite(5, TimeUnit.MINUTES)
                // 访问后过期时间（10分钟）
                .expireAfterAccess(10, TimeUnit.MINUTES)
                // 开启统计
                .recordStats());
        return cacheManager;
    }
    
    /**
     * 配置Caffeine对象
     * 可用于自定义缓存
     * @return Caffeine对象
     */
    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(5, TimeUnit.MINUTES);
    }
}
