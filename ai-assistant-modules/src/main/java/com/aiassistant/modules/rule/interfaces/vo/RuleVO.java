package com.aiassistant.modules.rule.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 规则视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleVO {

    /**
     * 规则ID
     */
    private Long id;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 规则描述
     */
    private String description;

    /**
     * 规则类型
     */
    private String ruleType;

    /**
     * 规则类型描述
     */
    private String ruleTypeDesc;

    /**
     * 规则表达式
     */
    private String expression;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 关联的规格ID
     */
    private Long specId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
