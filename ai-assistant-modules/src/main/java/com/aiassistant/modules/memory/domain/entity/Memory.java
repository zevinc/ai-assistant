package com.aiassistant.modules.memory.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 对话记忆实体
 * 用于存储会话中的对话上下文和历史信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_memory")
public class Memory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    @TableField("session_id")
    private String sessionId;

    /**
     * Agent ID
     */
    @TableField("agent_id")
    private Long agentId;

    /**
     * 角色（user/assistant/system）
     */
    @TableField("role")
    private String role;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 摘要
     */
    @TableField("summary")
    private String summary;

    /**
     * 重要性评分（1-10）
     */
    @TableField("importance")
    private Integer importance;

    /**
     * Token数量
     */
    @TableField("token_count")
    private Integer tokenCount;

    /**
     * 记忆类型（short_term/long_term/episodic/semantic）
     */
    @TableField("memory_type")
    private String memoryType;

    /**
     * 元数据（JSON格式）
     */
    @TableField("metadata")
    private String metadata;
}
