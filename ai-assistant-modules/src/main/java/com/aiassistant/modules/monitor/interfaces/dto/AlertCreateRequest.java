package com.aiassistant.modules.monitor.interfaces.dto;

import lombok.Data;

/**
 * 告警创建请求
 */
@Data
public class AlertCreateRequest {

    /**
     * 告警名称
     */
    private String alertName;

    /**
     * 关联指标名称
     */
    private String metricName;

    /**
     * 阈值
     */
    private Double threshold;

    /**
     * 比较操作符
     */
    private String operator;

    /**
     * 告警消息
     */
    private String message;
}
