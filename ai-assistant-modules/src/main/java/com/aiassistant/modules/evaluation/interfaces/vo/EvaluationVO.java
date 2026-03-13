package com.aiassistant.modules.evaluation.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评估视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationVO {

    /**
     * 评估ID
     */
    private Long id;

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
     * 评分
     */
    private Double score;

    /**
     * 评估指标
     */
    private String metrics;

    /**
     * 评估类型
     */
    private String evaluationType;

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
