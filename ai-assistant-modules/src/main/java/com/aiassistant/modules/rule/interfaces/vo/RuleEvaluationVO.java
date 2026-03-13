package com.aiassistant.modules.rule.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则评估结果VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleEvaluationVO {

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则表达式
     */
    private String expression;

    /**
     * 评估是否成功（通过）
     */
    private boolean success;

    /**
     * 评估消息
     */
    private String message;

    /**
     * 错误类型
     */
    private String error;
}
