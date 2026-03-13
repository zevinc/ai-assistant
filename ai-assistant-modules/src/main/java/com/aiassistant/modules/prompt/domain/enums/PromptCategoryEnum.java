package com.aiassistant.modules.prompt.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Prompt分类枚举
 */
@Getter
@AllArgsConstructor
public enum PromptCategoryEnum {

    /**
     * 系统提示
     */
    SYSTEM("system", "系统提示"),

    /**
     * 用户提示
     */
    USER("user", "用户提示"),

    /**
     * 助手提示
     */
    ASSISTANT("assistant", "助手提示"),

    /**
     * 函数提示
     */
    FUNCTION("function", "函数提示");

    private final String code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static PromptCategoryEnum fromCode(String code) {
        for (PromptCategoryEnum category : values()) {
            if (category.getCode().equals(code)) {
                return category;
            }
        }
        return null;
    }
}
