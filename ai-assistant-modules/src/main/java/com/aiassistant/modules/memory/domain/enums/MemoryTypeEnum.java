package com.aiassistant.modules.memory.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 记忆类型枚举
 */
@Getter
@AllArgsConstructor
public enum MemoryTypeEnum {

    /**
     * 短期记忆
     */
    SHORT_TERM("short_term", "短期记忆"),

    /**
     * 长期记忆
     */
    LONG_TERM("long_term", "长期记忆"),

    /**
     * 情景记忆
     */
    EPISODIC("episodic", "情景记忆"),

    /**
     * 语义记忆
     */
    SEMANTIC("semantic", "语义记忆");

    private final String code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static MemoryTypeEnum fromCode(String code) {
        for (MemoryTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
