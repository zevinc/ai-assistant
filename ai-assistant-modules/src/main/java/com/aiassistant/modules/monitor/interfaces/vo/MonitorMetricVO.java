package com.aiassistant.modules.monitor.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 监控指标视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorMetricVO {

    /**
     * 指标ID
     */
    private Long id;

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
     * 标签
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

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
