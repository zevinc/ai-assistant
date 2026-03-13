package com.aiassistant.modules.monitor.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.monitor.application.service.MonitorApplicationService;
import com.aiassistant.modules.monitor.domain.entity.MonitorAlert;
import com.aiassistant.modules.monitor.domain.entity.MonitorMetric;
import com.aiassistant.modules.monitor.interfaces.dto.AlertCreateRequest;
import com.aiassistant.modules.monitor.interfaces.dto.MetricRecordRequest;
import com.aiassistant.modules.monitor.interfaces.vo.MonitorAlertVO;
import com.aiassistant.modules.monitor.interfaces.vo.MonitorMetricVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 监控控制器
 * 提供系统监控和告警管理的REST API
 */
@RestController
@RequestMapping("/api/v1/monitor")
@Slf4j
@RequiredArgsConstructor
public class MonitorController {

    private final MonitorApplicationService monitorApplicationService;

    /**
     * 记录指标
     *
     * @param request 指标记录请求
     * @return 记录的指标
     */
    @PostMapping("/metrics")
    public Result<MonitorMetricVO> recordMetric(@RequestBody MetricRecordRequest request) {
        log.info("Recording metric: {} = {}", request.getMetricName(), request.getValue());

        MonitorMetric metric = monitorApplicationService.recordMetric(
                request.getMetricName(),
                request.getMetricType(),
                request.getValue(),
                request.getTags(),
                request.getAgentId(),
                request.getTimestamp()
        );

        return Result.ok(toMetricVO(metric));
    }

    /**
     * 批量记录指标
     *
     * @param requests 指标记录请求列表
     * @return 记录的指标列表
     */
    @PostMapping("/metrics/batch")
    public Result<List<MonitorMetricVO>> recordMetricsBatch(@RequestBody List<MetricRecordRequest> requests) {
        log.info("Recording batch metrics, count: {}", requests.size());

        List<MonitorApplicationService.MetricRecord> records = requests.stream()
                .map(r -> new MonitorApplicationService.MetricRecord(
                        r.getMetricName(),
                        r.getMetricType(),
                        r.getValue(),
                        r.getTags(),
                        r.getAgentId(),
                        r.getTimestamp()
                ))
                .collect(Collectors.toList());

        List<MonitorMetric> metrics = monitorApplicationService.recordMetricsBatch(records);

        List<MonitorMetricVO> voList = metrics.stream()
                .map(this::toMetricVO)
                .collect(Collectors.toList());

        return Result.ok(voList);
    }

    /**
     * 查询指标
     *
     * @param metricName 指标名称
     * @param start      开始时间
     * @param end        结束时间
     * @return 指标列表
     */
    @GetMapping("/metrics")
    public Result<List<MonitorMetricVO>> queryMetrics(
            @RequestParam String metricName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("Querying metrics: {} from {} to {}", metricName, start, end);

        List<MonitorMetric> metrics = monitorApplicationService.queryMetrics(metricName, start, end);

        List<MonitorMetricVO> voList = metrics.stream()
                .map(this::toMetricVO)
                .collect(Collectors.toList());

        return Result.ok(voList);
    }

    /**
     * 根据Agent ID查询指标
     *
     * @param agentId Agent ID
     * @return 指标列表
     */
    @GetMapping("/metrics/agent/{agentId}")
    public Result<List<MonitorMetricVO>> getMetricsByAgentId(@PathVariable Long agentId) {
        log.info("Getting metrics for agent: {}", agentId);

        List<MonitorMetric> metrics = monitorApplicationService.getMetricsByAgentId(agentId);

        List<MonitorMetricVO> voList = metrics.stream()
                .map(this::toMetricVO)
                .collect(Collectors.toList());

        return Result.ok(voList);
    }

    /**
     * 创建告警
     *
     * @param request 告警创建请求
     * @return 创建的告警
     */
    @PostMapping("/alerts")
    public Result<MonitorAlertVO> createAlert(@RequestBody AlertCreateRequest request) {
        log.info("Creating alert: {} for metric: {}", request.getAlertName(), request.getMetricName());

        MonitorAlert alert = monitorApplicationService.createAlert(
                request.getAlertName(),
                request.getMetricName(),
                request.getThreshold(),
                request.getOperator(),
                request.getMessage()
        );

        return Result.ok(toAlertVO(alert));
    }

    /**
     * 获取活跃告警列表
     *
     * @return 活跃告警列表
     */
    @GetMapping("/alerts/active")
    public Result<List<MonitorAlertVO>> getActiveAlerts() {
        log.info("Getting active alerts");

        List<MonitorAlert> alerts = monitorApplicationService.getActiveAlerts();

        List<MonitorAlertVO> voList = alerts.stream()
                .map(this::toAlertVO)
                .collect(Collectors.toList());

        return Result.ok(voList);
    }

    /**
     * 获取告警详情
     *
     * @param id 告警ID
     * @return 告警详情
     */
    @GetMapping("/alerts/{id}")
    public Result<MonitorAlertVO> getAlert(@PathVariable Long id) {
        log.info("Getting alert: {}", id);

        MonitorAlert alert = monitorApplicationService.getAlert(id);

        return Result.ok(toAlertVO(alert));
    }

    /**
     * 解决告警
     *
     * @param id 告警ID
     * @return 更新后的告警
     */
    @PostMapping("/alerts/{id}/resolve")
    public Result<MonitorAlertVO> resolveAlert(@PathVariable Long id) {
        log.info("Resolving alert: {}", id);

        MonitorAlert alert = monitorApplicationService.resolveAlert(id);

        return Result.ok(toAlertVO(alert));
    }

    /**
     * 静默告警
     *
     * @param id 告警ID
     * @return 更新后的告警
     */
    @PostMapping("/alerts/{id}/silence")
    public Result<MonitorAlertVO> silenceAlert(@PathVariable Long id) {
        log.info("Silencing alert: {}", id);

        MonitorAlert alert = monitorApplicationService.silenceAlert(id);

        return Result.ok(toAlertVO(alert));
    }

    /**
     * 将指标实体转换为VO
     */
    private MonitorMetricVO toMetricVO(MonitorMetric metric) {
        return MonitorMetricVO.builder()
                .id(metric.getId())
                .metricName(metric.getMetricName())
                .metricType(metric.getMetricType())
                .value(metric.getValue())
                .tags(metric.getTags())
                .agentId(metric.getAgentId())
                .timestamp(metric.getTimestamp())
                .createdAt(metric.getCreatedAt())
                .build();
    }

    /**
     * 将告警实体转换为VO
     */
    private MonitorAlertVO toAlertVO(MonitorAlert alert) {
        return MonitorAlertVO.builder()
                .id(alert.getId())
                .alertName(alert.getAlertName())
                .metricName(alert.getMetricName())
                .threshold(alert.getThreshold())
                .operator(alert.getOperator())
                .message(alert.getMessage())
                .status(alert.getStatus())
                .triggeredAt(alert.getTriggeredAt())
                .resolvedAt(alert.getResolvedAt())
                .createdAt(alert.getCreatedAt())
                .updatedAt(alert.getUpdatedAt())
                .build();
    }
}
