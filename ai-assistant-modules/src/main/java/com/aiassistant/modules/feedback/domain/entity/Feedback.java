package com.aiassistant.modules.feedback.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 反馈实体
 * 用于存储用户反馈信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_feedback")
public class Feedback extends BaseEntity {

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
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 消息ID
     */
    @TableField("message_id")
    private String messageId;

    /**
     * 评分（1-5）
     */
    @TableField("rating")
    private Integer rating;

    /**
     * 评论内容
     */
    @TableField("comment")
    private String comment;

    /**
     * 反馈类型
     */
    @TableField("feedback_type")
    private String feedbackType;

    /**
     * 状态（0-待处理, 1-已处理）
     */
    @TableField("status")
    private Integer status;
}
