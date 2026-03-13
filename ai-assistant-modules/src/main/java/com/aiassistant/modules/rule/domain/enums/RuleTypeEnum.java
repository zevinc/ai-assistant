package com.aiassistant.modules.rule.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规则类型枚举
 */
@Getter
@AllArgsConstructor
public enum RuleTypeEnum {

    /**
     * 前置检查规则
     */
    PRE_CHECK("pre_check", "前置检查"),

    /**
     * 后置检查规则
     */
    POST_CHECK("post_check", "后置检查"),

    /**
     * 过滤规则
     */
    FILTER("filter", "过滤"),

    /**
     * 转换规则
     */
    TRANSFORM("transform", "转换");

    /**
     * 类型编码
     */
    private final String code;

    /**
     * 类型描述
     */
    private final String description;

    /**
     * 根据编码获取枚举
     *
     * @param code 类型编码
     * @return 枚举值
     */
    public static RuleTypeEnum getByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        for (RuleTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断是否为有效编码
     *
     * @param code 类型编码
     * @return 是否有效
     */
    public static boolean isValidCode(String code) {
        return getByCode(code) != null;
    }
}
