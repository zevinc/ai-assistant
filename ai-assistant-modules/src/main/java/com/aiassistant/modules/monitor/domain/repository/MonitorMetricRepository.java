package com.aiassistant.modules.monitor.domain.repository;

import com.aiassistant.modules.monitor.domain.entity.MonitorMetric;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 监控指标仓储接口
 */
public interface MonitorMetricRepository {

    /**
     * 保存指标
     *
     * @param metric 指标实体
     * @return 保存后的指标
     */
    MonitorMetric save(MonitorMetric metric);

    /**
     * 批量保存指标
     *
     * @param metrics 指标列表
     * @return 保存后的指标列表
     */
    List<MonitorMetric> saveBatch(List<MonitorMetric> metrics);

    /**
     * 根据指标名称和时间范围查询
     *
     * @param metricName 指标名称
     * @param start      开始时间
     * @param end        结束时间
     * @return 指标列表
     */
    List<MonitorMetric> findByMetricName(String metricName, LocalDateTime start, LocalDateTime end);

    /**
     * 根据Agent ID查询指标
     *
     * @param agentId Agent ID
     * @return 指标列表
     */
    List<MonitorMetric> findByAgentId(Long agentId);
}
