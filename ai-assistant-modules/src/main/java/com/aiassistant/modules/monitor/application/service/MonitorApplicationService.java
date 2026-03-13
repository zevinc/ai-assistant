package com.aiassistant.modules.monitor.application.service;

import com.aiassistant.common.event.DomainEventPublisher;
import com.aiassistant.common.event.SimpleDomainEvent;
import com.aiassistant.modules.monitor.domain.entity.MonitorAlert;
import com.aiassistant.modules.monitor.domain.entity.MonitorMetric;
import com.aiassistant.modules.monitor.domain.enums.AlertStatusEnum;
import com.aiassistant.modules.monitor.domain.repository.MonitorAlertRepository;
import com.aiassistant.modules.monitor.domain.repository.MonitorMetricRepository;
import com.aiassistant.modules.monitor.domain.service.AlertEvaluator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 监控应用服务
 * 提供系统监控和告警管理的业务用例
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MonitorApplicationService {

    private final MonitorMetricRepository metricRepository;
    private final MonitorAlertRepository alertRepository;
    private final AlertEvaluator alertEvaluator;
    private final DomainEventPublisher eventPublisher;

    /**
     * 记录指标
     *
     * @param metricName 指标名称
     * @param metricType 指标类型
     * @param value      指标值
     * @param tags       标签
     * @param agentId    Agent ID
     * @param timestamp  时间戳
     * @return 记录的指标
     */
    @Transactional
    public MonitorMetric recordMetric(String metricName, String metricType, Double value,
                                      String tags, Long agentId, LocalDateTime timestamp) {
        log.debug("Recording metric: {} = {}", metricName, value);

        MonitorMetric metric = MonitorMetric.builder()
                .metricName(metricName)
                .metricType(metricType)
                .value(value)
                .tags(tags)
                .agentId(agentId)
                .timestamp(timestamp != null ? timestamp : LocalDateTime.now())
                .build();

        MonitorMetric savedMetric = metricRepository.save(metric);

        // 检查是否触发告警
        checkAndTriggerAlerts(metricName, value);

        log.debug("Metric recorded successfully with id: {}", savedMetric.getId());
        return savedMetric;
    }

    /**
     * 批量记录指标
     *
     * @param metrics 指标列表
     * @return 记录的指标列表
     */
    @Transactional
    public List<MonitorMetric> recordMetricsBatch(List<MetricRecord> metrics) {
        log.info("Recording batch metrics, count: {}", metrics.size());

        List<MonitorMetric> metricEntities = new ArrayList<>();
        for (MetricRecord record : metrics) {
            MonitorMetric metric = MonitorMetric.builder()
                    .metricName(record.metricName())
                    .metricType(record.metricType())
                    .value(record.value())
                    .tags(record.tags())
                    .agentId(record.agentId())
                    .timestamp(record.timestamp() != null ? record.timestamp() : LocalDateTime.now())
                    .build();
            metricEntities.add(metric);
        }

        List<MonitorMetric> savedMetrics = metricRepository.saveBatch(metricEntities);

        log.info("Batch metrics recorded, count: {}", savedMetrics.size());
        return savedMetrics;
    }

    /**
     * 查询指标
     *
     * @param metricName 指标名称
     * @param start      开始时间
     * @param end        结束时间
     * @return 指标列表
     */
    public List<MonitorMetric> queryMetrics(String metricName, LocalDateTime start, LocalDateTime end) {
        return metricRepository.findByMetricName(metricName, start, end);
    }

    /**
     * 根据Agent ID查询指标
     *
     * @param agentId Agent ID
     * @return 指标列表
     */
    public List<MonitorMetric> getMetricsByAgentId(Long agentId) {
        return metricRepository.findByAgentId(agentId);
    }

    /**
     * 创建告警
     *
     * @param alertName  告警名称
     * @param metricName 指标名称
     * @param threshold  阈值
     * @param operator   操作符
     * @param message    告警消息
     * @return 创建的告警
     */
    @Transactional
    public MonitorAlert createAlert(String alertName, String metricName, Double threshold,
                                    String operator, String message) {
        log.info("Creating alert: {} for metric: {}", alertName, metricName);

        MonitorAlert alert = MonitorAlert.builder()
                .alertName(alertName)
                .metricName(metricName)
                .threshold(threshold)
                .operator(operator)
                .message(message)
                .status(AlertStatusEnum.ACTIVE.getCode())
                .build();

        MonitorAlert savedAlert = alertRepository.save(alert);

        // 发布告警创建事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(savedAlert.getId()), "MonitorAlert", "AlertCreated"));

        log.info("Alert created successfully with id: {}", savedAlert.getId());
        return savedAlert;
    }

    /**
     * 获取活跃告警列表
     *
     * @return 活跃告警列表
     */
    public List<MonitorAlert> getActiveAlerts() {
        return alertRepository.findActiveAlerts();
    }

    /**
     * 获取告警详情
     *
     * @param id 告警ID
     * @return 告警实体
     */
    public MonitorAlert getAlert(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found: " + id));
    }

    /**
     * 解决告警
     *
     * @param id 告警ID
     * @return 更新后的告警
     */
    @Transactional
    public MonitorAlert resolveAlert(Long id) {
        log.info("Resolving alert: {}", id);

        MonitorAlert alert = alertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found: " + id));

        alert.setStatus(AlertStatusEnum.RESOLVED.getCode());
        alert.setResolvedAt(LocalDateTime.now());

        MonitorAlert updatedAlert = alertRepository.update(alert);

        // 发布告警解决事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(id), "MonitorAlert", "AlertResolved"));

        log.info("Alert resolved successfully: {}", id);
        return updatedAlert;
    }

    /**
     * 静默告警
     *
     * @param id 告警ID
     * @return 更新后的告警
     */
    @Transactional
    public MonitorAlert silenceAlert(Long id) {
        log.info("Silencing alert: {}", id);

        MonitorAlert alert = alertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found: " + id));

        alert.setStatus(AlertStatusEnum.SILENCED.getCode());

        MonitorAlert updatedAlert = alertRepository.update(alert);

        log.info("Alert silenced successfully: {}", id);
        return updatedAlert;
    }

    /**
     * 检查并触发告警
     *
     * @param metricName 指标名称
     * @param value      当前值
     */
    private void checkAndTriggerAlerts(String metricName, Double value) {
        List<MonitorAlert> activeAlerts = alertRepository.findActiveAlerts();

        for (MonitorAlert alert : activeAlerts) {
            if (metricName.equals(alert.getMetricName())) {
                boolean triggered = alertEvaluator.checkAlert(alert, value);

                if (triggered) {
                    log.warn("Alert triggered: {} for metric {} with value {}",
                            alert.getAlertName(), metricName, value);

                    alert.setTriggeredAt(LocalDateTime.now());
                    alertRepository.update(alert);

                    // 发布告警触发事件
                    eventPublisher.publish(new SimpleDomainEvent(String.valueOf(alert.getId()), "MonitorAlert", "AlertTriggered"));
                }
            }
        }
    }

    /**
     * 指标记录内部记录
     */
    public record MetricRecord(String metricName, String metricType, Double value,
                               String tags, Long agentId, LocalDateTime timestamp) {}
}
