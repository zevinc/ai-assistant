package com.aiassistant.modules.agent.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Agent状态枚举
 */
@Getter
@AllArgsConstructor
public enum AgentStatusEnum {

    /**
     * 草稿状态
     */
    DRAFT(0, "草稿"),

    /**
     * 激活状态
     */
    ACTIVE(1, "激活"),

    /**
     * 停用状态
     */
    INACTIVE(2, "停用");

    private final int code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static AgentStatusEnum fromCode(int code) {
        for (AgentStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}
