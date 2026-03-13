package com.aiassistant.modules.monitor.interfaces.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 指标记录请求
 */
@Data
public class MetricRecordRequest {

    /**
     * 指标名称
     */
    private String metricName;

    /**
     * 指标类型
     */
    private String metricType;

    /**
     * 指标值
     */
    private Double value;

    /**
     * 标签（JSON格式）
     */
    private String tags;

    /**
     * Agent ID
     */
    private Long agentId;

    /**
     * 时间戳
     */
    private LocalDateTime timestamp;
}
