package com.aiassistant.modules.agent.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会话状态枚举
 */
@Getter
@AllArgsConstructor
public enum SessionStatusEnum {

    /**
     * 活跃状态
     */
    ACTIVE("active", "活跃"),

    /**
     * 已关闭
     */
    CLOSED("closed", "已关闭"),

    /**
     * 已归档
     */
    ARCHIVED("archived", "已归档");

    private final String code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static SessionStatusEnum fromCode(String code) {
        for (SessionStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
