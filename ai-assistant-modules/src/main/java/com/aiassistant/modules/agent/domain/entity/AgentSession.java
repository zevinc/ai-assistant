package com.aiassistant.modules.agent.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Agent会话实体
 * 用于存储Agent会话信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_agent_session")
public class AgentSession extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Agent ID
     */
    @TableField("agent_id")
    private Long agentId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 会话ID（唯一标识）
     */
    @TableField("session_id")
    private String sessionId;

    /**
     * 会话标题
     */
    @TableField("title")
    private String title;

    /**
     * 会话状态（active/closed/archived）
     */
    @TableField("status")
    private String status;

    /**
     * 消息数量
     */
    @TableField("message_count")
    private Integer messageCount;
}
