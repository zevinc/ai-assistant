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
 * 监控指标实体
 * 用于存储系统监控指标数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_monitor_metric")
public class MonitorMetric extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 指标名称
     */
    @TableField("metric_name")
    private String metricName;

    /**
     * 指标类型
     */
    @TableField("metric_type")
    private String metricType;

    /**
     * 指标值
     */
    @TableField("value")
    private Double value;

    /**
     * 标签（JSON格式）
     */
    @TableField("tags")
    private String tags;

    /**
     * Agent ID
     */
    @TableField("agent_id")
    private Long agentId;

    /**
     * 时间戳
     */
    @TableField("timestamp")
    private LocalDateTime timestamp;
}
