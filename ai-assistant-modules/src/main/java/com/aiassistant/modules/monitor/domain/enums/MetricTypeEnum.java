package com.aiassistant.modules.monitor.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 指标类型枚举
 */
@Getter
@AllArgsConstructor
public enum MetricTypeEnum {

    /**
     * 计数器
     */
    COUNTER("counter", "计数器"),

    /**
     * 仪表盘
     */
    GAUGE("gauge", "仪表盘"),

    /**
     * 直方图
     */
    HISTOGRAM("histogram", "直方图"),

    /**
     * 计时器
     */
    TIMER("timer", "计时器");

    private final String code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static MetricTypeEnum fromCode(String code) {
        for (MetricTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
