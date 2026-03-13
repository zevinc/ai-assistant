package com.aiassistant.modules.rule.domain.service;

import com.aiassistant.modules.rule.domain.entity.Rule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 规则评估器
 * 基于SpEL表达式评估规则
 */
@Slf4j
@Service
public class RuleEvaluator {

    private final ExpressionParser expressionParser = new SpelExpressionParser();

    /**
     * 评估规则表达式
     *
     * @param rule 规则实体
     * @param context 评估上下文
     * @return 评估结果
     */
    public boolean evaluate(Rule rule, Map<String, Object> context) {
        if (rule == null) {
            log.warn("规则为空，评估失败");
            return false;
        }

        if (rule.getExpression() == null || rule.getExpression().trim().isEmpty()) {
            log.warn("规则表达式为空，规则ID: {}", rule.getId());
            return true; // 空表达式默认通过
        }

        try {
            // 创建评估上下文
            StandardEvaluationContext evaluationContext = new StandardEvaluationContext();

            // 将上下文变量设置到评估上下文中
            if (context != null) {
                context.forEach(evaluationContext::setVariable);
            }

            // 解析表达式
            Expression expression = expressionParser.parseExpression(rule.getExpression());

            // 执行评估
            Boolean result = expression.getValue(evaluationContext, Boolean.class);

            log.debug("规则评估完成: 规则ID={}, 规则名称={}, 结果={}",
                    rule.getId(), rule.getName(), result);

            return result != null && result;

        } catch (Exception e) {
            log.error("规则评估失败: 规则ID={}, 规则名称={}, 表达式={}, 错误: {}",
                    rule.getId(), rule.getName(), rule.getExpression(), e.getMessage());
            return false;
        }
    }

    /**
     * 验证表达式语法
     *
     * @param expression 表达式
     * @return 是否有效
     */
    public boolean validateExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return true; // 空表达式视为有效
        }

        try {
            expressionParser.parseExpression(expression);
            return true;
        } catch (Exception e) {
            log.warn("表达式语法无效: {}, 错误: {}", expression, e.getMessage());
            return false;
        }
    }

    /**
     * 带详细结果的评估
     *
     * @param rule 规则实体
     * @param context 评估上下文
     * @return 评估结果
     */
    public EvaluationResult evaluateWithDetails(Rule rule, Map<String, Object> context) {
        EvaluationResult result = new EvaluationResult();
        result.setRuleId(rule.getId());
        result.setRuleName(rule.getName());
        result.setExpression(rule.getExpression());

        if (rule.getExpression() == null || rule.getExpression().trim().isEmpty()) {
            result.setSuccess(true);
            result.setMessage("表达式为空，默认通过");
            return result;
        }

        try {
            StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
            if (context != null) {
                context.forEach(evaluationContext::setVariable);
            }

            Expression expression = expressionParser.parseExpression(rule.getExpression());
            Boolean evalResult = expression.getValue(evaluationContext, Boolean.class);

            result.setSuccess(evalResult != null && evalResult);
            result.setMessage("评估成功");

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("评估失败: " + e.getMessage());
            result.setError(e.getClass().getSimpleName());
        }

        return result;
    }

    /**
     * 评估结果详情
     */
    public static class EvaluationResult {
        private Long ruleId;
        private String ruleName;
        private String expression;
        private boolean success;
        private String message;
        private String error;

        public Long getRuleId() {
            return ruleId;
        }

        public void setRuleId(Long ruleId) {
            this.ruleId = ruleId;
        }

        public String getRuleName() {
            return ruleName;
        }

        public void setRuleName(String ruleName) {
            this.ruleName = ruleName;
        }

        public String getExpression() {
            return expression;
        }

        public void setExpression(String expression) {
            this.expression = expression;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
