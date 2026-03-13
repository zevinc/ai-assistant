package com.aiassistant.modules.evaluation.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评估类型枚举
 */
@Getter
@AllArgsConstructor
public enum EvaluationTypeEnum {

    /**
     * 自动评估
     */
    AUTOMATED("automated", "自动评估"),

    /**
     * 人工评估
     */
    HUMAN("human", "人工评估"),

    /**
     * A/B测试
     */
    A_B_TEST("ab_test", "A/B测试");

    private final String code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static EvaluationTypeEnum fromCode(String code) {
        for (EvaluationTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
