package com.aiassistant.modules.feedback.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 反馈类型枚举
 */
@Getter
@AllArgsConstructor
public enum FeedbackTypeEnum {

    /**
     * 点赞
     */
    THUMBS_UP("thumbs_up", "点赞"),

    /**
     * 点踩
     */
    THUMBS_DOWN("thumbs_down", "点踩"),

    /**
     * 评分
     */
    RATING("rating", "评分"),

    /**
     * 文本反馈
     */
    TEXT("text", "文本反馈");

    private final String code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static FeedbackTypeEnum fromCode(String code) {
        for (FeedbackTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
