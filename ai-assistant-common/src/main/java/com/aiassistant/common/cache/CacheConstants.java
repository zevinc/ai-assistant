package com.aiassistant.common.cache;

/**
 * 缓存常量
 * 定义系统中使用的缓存键前缀
 */
public final class CacheConstants {
    
    private CacheConstants() {
        // 私有构造函数，防止实例化
    }
    
    /**
     * Agent配置缓存前缀
     */
    public static final String AGENT_CONFIG = "agent:config:";
    
    /**
     * 短期记忆缓存前缀
     */
    public static final String MEMORY_SHORT = "memory:short:";
    
    /**
     * 活跃规则缓存前缀
     */
    public static final String RULE_ACTIVE = "rule:active:";
    
    /**
     * 技能元数据缓存前缀
     */
    public static final String SKILL_META = "skill:meta:";
    
    /**
     * 会话缓存前缀
     */
    public static final String SESSION = "session:";
    
    /**
     * LLM速率限制缓存前缀
     */
    public static final String LLM_RATE = "llm:rate:";
    
    /**
     * 默认缓存过期时间（秒）
     */
    public static final long DEFAULT_TTL = 3600L;
    
    /**
     * 短期缓存过期时间（秒）- 5分钟
     */
    public static final long SHORT_TTL = 300L;
    
    /**
     * 长期缓存过期时间（秒）- 24小时
     */
    public static final long LONG_TTL = 86400L;
}
