package com.aiassistant.modules.monitor.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 监控告警视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitorAlertVO {

    /**
     * 告警ID
     */
    private Long id;

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

    /**
     * 状态
     */
    private String status;

    /**
     * 触发时间
     */
    private LocalDateTime triggeredAt;

    /**
     * 解决时间
     */
    private LocalDateTime resolvedAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
