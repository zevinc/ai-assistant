package com.aiassistant.common.cache;

/**
 * 多级缓存接口
 * 支持L1本地缓存和L2分布式缓存
 */
public interface MultiLevelCache {
    
    /**
     * 从缓存获取数据
     * @param key 缓存键
     * @param type 数据类型
     * @param <T> 数据类型
     * @return 缓存数据，不存在返回null
     */
    <T> T get(String key, Class<T> type);
    
    /**
     * 将数据存入缓存
     * @param key 缓存键
     * @param value 缓存值
     * @param ttlSeconds 过期时间（秒）
     */
    void put(String key, Object value, long ttlSeconds);
    
    /**
     * 从缓存移除数据
     * @param key 缓存键
     */
    void evict(String key);
    
    /**
     * 清空所有缓存
     */
    default void clear() {
        // 默认空实现
    }
    
    /**
     * 检查缓存是否存在
     * @param key 缓存键
     * @return 是否存在
     */
    default boolean exists(String key) {
        return get(key, Object.class) != null;
    }
}
