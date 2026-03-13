package com.aiassistant.modules.evaluation.interfaces.dto;

import lombok.Data;

/**
 * 评估创建请求
 */
@Data
public class EvaluationCreateRequest {

    /**
     * Agent ID
     */
    private Long agentId;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 输入文本
     */
    private String inputText;

    /**
     * 输出文本
     */
    private String outputText;

    /**
     * 期望输出
     */
    private String expectedOutput;

    /**
     * 评估类型
     */
    private String evaluationType;

    /**
     * 评估指标（JSON格式）
     */
    private String metrics;
}
