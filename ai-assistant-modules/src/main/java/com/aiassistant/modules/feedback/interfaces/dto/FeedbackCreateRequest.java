package com.aiassistant.modules.feedback.interfaces.dto;

import lombok.Data;

/**
 * 反馈创建请求
 */
@Data
public class FeedbackCreateRequest {

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
}
