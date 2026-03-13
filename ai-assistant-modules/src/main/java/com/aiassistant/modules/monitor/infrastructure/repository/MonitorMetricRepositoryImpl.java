package com.aiassistant.modules.monitor.infrastructure.repository;

import com.aiassistant.modules.monitor.domain.entity.MonitorMetric;
import com.aiassistant.modules.monitor.domain.repository.MonitorMetricRepository;
import com.aiassistant.modules.monitor.infrastructure.mapper.MonitorMetricMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 监控指标仓储实现
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class MonitorMetricRepositoryImpl implements MonitorMetricRepository {

    private final MonitorMetricMapper monitorMetricMapper;

    @Override
    public MonitorMetric save(MonitorMetric metric) {
        monitorMetricMapper.insert(metric);
        return metric;
    }

    @Override
    public List<MonitorMetric> saveBatch(List<MonitorMetric> metrics) {
        for (MonitorMetric metric : metrics) {
            monitorMetricMapper.insert(metric);
        }
        return metrics;
    }

    @Override
    public List<MonitorMetric> findByMetricName(String metricName, LocalDateTime start, LocalDateTime end) {
        LambdaQueryWrapper<MonitorMetric> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MonitorMetric::getMetricName, metricName)
                .ge(start != null, MonitorMetric::getTimestamp, start)
                .le(end != null, MonitorMetric::getTimestamp, end)
                .orderByDesc(MonitorMetric::getTimestamp);
        return monitorMetricMapper.selectList(queryWrapper);
    }

    @Override
    public List<MonitorMetric> findByAgentId(Long agentId) {
        LambdaQueryWrapper<MonitorMetric> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MonitorMetric::getAgentId, agentId)
                .orderByDesc(MonitorMetric::getTimestamp);
        return monitorMetricMapper.selectList(queryWrapper);
    }
}
