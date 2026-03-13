package com.aiassistant.modules.memory.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 记忆视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoryVO {

    /**
     * 记忆ID
     */
    private Long id;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * Agent ID
     */
    private Long agentId;

    /**
     * 角色
     */
    private String role;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 重要性评分
     */
    private Integer importance;

    /**
     * Token数量
     */
    private Integer tokenCount;

    /**
     * 记忆类型
     */
    private String memoryType;

    /**
     * 元数据
     */
    private String metadata;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
