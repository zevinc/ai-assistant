package com.aiassistant.modules.plan.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 计划状态枚举
 */
@Getter
@AllArgsConstructor
public enum PlanStatusEnum {

    /**
     * 已创建
     */
    CREATED("created", "已创建"),

    /**
     * 运行中
     */
    RUNNING("running", "运行中"),

    /**
     * 已暂停
     */
    PAUSED("paused", "已暂停"),

    /**
     * 已完成
     */
    COMPLETED("completed", "已完成"),

    /**
     * 已失败
     */
    FAILED("failed", "已失败");

    private final String code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static PlanStatusEnum fromCode(String code) {
        for (PlanStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
