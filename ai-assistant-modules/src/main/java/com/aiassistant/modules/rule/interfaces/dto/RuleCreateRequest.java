package com.aiassistant.modules.rule.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规则创建请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleCreateRequest {

    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空")
    private String name;

    /**
     * 规则描述
     */
    private String description;

    /**
     * 规则类型
     */
    @NotBlank(message = "规则类型不能为空")
    private String ruleType;

    /**
     * 规则表达式
     */
    @NotBlank(message = "规则表达式不能为空")
    private String expression;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 关联的规格ID
     */
    @NotNull(message = "规格ID不能为空")
    private Long specId;
}
