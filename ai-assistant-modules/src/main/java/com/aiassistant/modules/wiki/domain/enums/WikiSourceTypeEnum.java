package com.aiassistant.modules.wiki.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 知识库来源类型枚举
 */
@Getter
@AllArgsConstructor
public enum WikiSourceTypeEnum {

    /**
     * 手动录入
     */
    MANUAL("manual", "手动录入"),

    /**
     * URL导入
     */
    URL("url", "URL导入"),

    /**
     * 文件上传
     */
    FILE("file", "文件上传"),

    /**
     * API导入
     */
    API("api", "API导入");

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
    public static WikiSourceTypeEnum getByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        for (WikiSourceTypeEnum type : values()) {
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
