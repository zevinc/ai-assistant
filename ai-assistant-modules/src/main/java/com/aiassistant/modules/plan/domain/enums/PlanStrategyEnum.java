package com.aiassistant.modules.plan.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 计划策略枚举
 */
@Getter
@AllArgsConstructor
public enum PlanStrategyEnum {

    /**
     * ReAct策略（推理-行动循环）
     */
    REACT("react", "ReAct策略"),

    /**
     * 计划-执行策略
     */
    PLAN_EXECUTE("plan_execute", "计划-执行策略"),

    /**
     * 层次化策略
     */
    HIERARCHICAL("hierarchical", "层次化策略");

    private final String code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static PlanStrategyEnum fromCode(String code) {
        for (PlanStrategyEnum strategy : values()) {
            if (strategy.getCode().equals(code)) {
                return strategy;
            }
        }
        return null;
    }
}
