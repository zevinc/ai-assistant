package com.aiassistant.modules.spec.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 规格状态枚举
 */
@Getter
@AllArgsConstructor
public enum SpecStatusEnum {

    /**
     * 草稿状态
     */
    DRAFT(0, "草稿"),

    /**
     * 已发布状态
     */
    PUBLISHED(1, "已发布"),

    /**
     * 已归档状态
     */
    ARCHIVED(2, "已归档");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态描述
     */
    private final String description;

    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return 枚举值
     */
    public static SpecStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SpecStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断是否为有效状态码
     *
     * @param code 状态码
     * @return 是否有效
     */
    public static boolean isValidCode(Integer code) {
        return getByCode(code) != null;
    }
}
