package com.aiassistant.modules.feedback.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 反馈视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackVO {

    /**
     * 反馈ID
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
     * 用户ID
     */
    private Long userId;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 评分（1-5）
     */
    private Integer rating;

    /**
     * 评论内容
     */
    private String comment;

    /**
     * 反馈类型
     */
    private String feedbackType;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
