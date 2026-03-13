package com.aiassistant.modules.monitor.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 监控告警实体
 * 用于存储系统告警配置和状态
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_monitor_alert")
public class MonitorAlert extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 告警名称
     */
    @TableField("alert_name")
    private String alertName;

    /**
     * 关联指标名称
     */
    @TableField("metric_name")
    private String metricName;

    /**
     * 阈值
     */
    @TableField("threshold")
    private Double threshold;

    /**
     * 比较操作符（>, <, >=, <=, ==, !=）
     */
    @TableField("operator")
    private String operator;

    /**
     * 告警消息
     */
    @TableField("message")
    private String message;

    /**
     * 状态（active, resolved, silenced）
     */
    @TableField("status")
    private String status;

    /**
     * 触发时间
     */
    @TableField("triggered_at")
    private LocalDateTime triggeredAt;

    /**
     * 解决时间
     */
    @TableField("resolved_at")
    private LocalDateTime resolvedAt;
}
