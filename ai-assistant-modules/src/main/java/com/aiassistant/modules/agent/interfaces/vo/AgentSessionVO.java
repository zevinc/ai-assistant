package com.aiassistant.modules.agent.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Agent会话视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentSessionVO {

    /**
     * 会话ID（数据库主键）
     */
    private Long id;

    /**
     * Agent ID
     */
    private Long agentId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 会话ID（唯一标识）
     */
    private String sessionId;

    /**
     * 会话标题
     */
    private String title;

    /**
     * 会话状态
     */
    private String status;

    /**
     * 消息数量
     */
    private Integer messageCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
