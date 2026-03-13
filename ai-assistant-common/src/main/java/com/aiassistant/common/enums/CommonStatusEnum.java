package com.aiassistant.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum {
    
    /**
     * 启用
     */
    ENABLED(1, "启用"),
    
    /**
     * 禁用
     */
    DISABLED(0, "禁用");
    
    /**
     * 状态码
     */
    private final int code;
    
    /**
     * 状态描述
     */
    private final String description;
    
    /**
     * 根据状态码获取枚举
     * @param code 状态码
     * @return 枚举值
     */
    public static CommonStatusEnum fromCode(int code) {
        for (CommonStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * 判断是否启用
     * @param code 状态码
     * @return 是否启用
     */
    public static boolean isEnabled(int code) {
        return ENABLED.getCode() == code;
    }
    
    /**
     * 判断是否禁用
     * @param code 状态码
     * @return 是否禁用
     */
    public static boolean isDisabled(int code) {
        return DISABLED.getCode() == code;
    }
}
