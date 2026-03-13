package com.aiassistant.modules.rule.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 规则评估请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleEvaluateRequest {

    /**
     * 评估上下文变量
     */
    private Map<String, Object> context;
}
