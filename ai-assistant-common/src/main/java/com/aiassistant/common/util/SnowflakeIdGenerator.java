package com.aiassistant.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 雪花算法ID生成器
 * 生成唯一的长整型ID
 */
@Slf4j
public class SnowflakeIdGenerator {
    
    /**
     * 起始时间戳（2024-01-01 00:00:00）
     */
    private static final long START_TIMESTAMP = 1704038400000L;
    
    /**
     * 数据中心ID所占位数
     */
    private static final long DATACENTER_ID_BITS = 5L;
    
    /**
     * 机器ID所占位数
     */
    private static final long WORKER_ID_BITS = 5L;
    
    /**
     * 序列号所占位数
     */
    private static final long SEQUENCE_BITS = 12L;
    
    /**
     * 数据中心ID最大值
     */
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    
    /**
     * 机器ID最大值
     */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    
    /**
     * 序列号最大值
     */
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    
    /**
     * 机器ID左移位数
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    
    /**
     * 数据中心ID左移位数
     */
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    
    /**
     * 时间戳左移位数
     */
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    
    /**
     * 数据中心ID
     */
    private final long datacenterId;
    
    /**
     * 机器ID
     */
    private final long workerId;
    
    /**
     * 序列号
     */
    private long sequence = 0L;
    
    /**
     * 上次生成ID的时间戳
     */
    private long lastTimestamp = -1L;
    
    /**
     * 默认静态实例
     */
    private static final SnowflakeIdGenerator DEFAULT_INSTANCE = new SnowflakeIdGenerator(1, 1);
    
    /**
     * 构造函数
     * @param datacenterId 数据中心ID（0-31）
     * @param workerId 机器ID（0-31）
     */
    public SnowflakeIdGenerator(long datacenterId, long workerId) {
        if (datacenterId < 0 || datacenterId > MAX_DATACENTER_ID) {
            throw new IllegalArgumentException(
                    String.format("数据中心ID必须在0-%d之间", MAX_DATACENTER_ID));
        }
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException(
                    String.format("机器ID必须在0-%d之间", MAX_WORKER_ID));
        }
        this.datacenterId = datacenterId;
        this.workerId = workerId;
        log.info("雪花算法ID生成器初始化完成: datacenterId={}, workerId={}", datacenterId, workerId);
    }
    
    /**
     * 生成下一个ID
     * @return 唯一ID
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();
        
        // 时钟回拨检测
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            if (offset <= 5) {
                // 小范围回拨，等待时钟追上
                try {
                    wait(offset << 1);
                    timestamp = System.currentTimeMillis();
                    if (timestamp < lastTimestamp) {
                        throw new RuntimeException("时钟回拨，拒绝生成ID");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("时钟回拨，拒绝生成ID", e);
                }
            } else {
                throw new RuntimeException("时钟回拨，拒绝生成ID");
            }
        }
        
        if (timestamp == lastTimestamp) {
            // 同一毫秒内，序列号递增
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                // 序列号溢出，等待下一毫秒
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            // 不同毫秒，序列号重置
            sequence = 0L;
        }
        
        lastTimestamp = timestamp;
        
        // 组装ID
        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }
    
    /**
     * 等待下一毫秒
     * @param lastTimestamp 上次时间戳
     * @return 新的时间戳
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
    
    /**
     * 使用默认实例生成ID
     * @return 唯一ID
     */
    public static long generateId() {
        return DEFAULT_INSTANCE.nextId();
    }
    
    /**
     * 获取默认实例
     * @return 默认实例
     */
    public static SnowflakeIdGenerator getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }
}
