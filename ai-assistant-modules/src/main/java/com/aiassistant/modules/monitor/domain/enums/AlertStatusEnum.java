package com.aiassistant.modules.monitor.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 告警状态枚举
 */
@Getter
@AllArgsConstructor
public enum AlertStatusEnum {

    /**
     * 活跃状态
     */
    ACTIVE("active", "活跃"),

    /**
     * 已解决
     */
    RESOLVED("resolved", "已解决"),

    /**
     * 静默状态
     */
    SILENCED("silenced", "静默");

    private final String code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static AlertStatusEnum fromCode(String code) {
        for (AlertStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
