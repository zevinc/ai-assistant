package com.aiassistant.modules.monitor.domain.service;

import com.aiassistant.modules.monitor.domain.entity.MonitorAlert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 告警评估器
 * 用于评估指标值是否触发告警
 */
@Service
@Slf4j
public class AlertEvaluator {

    /**
     * 检查是否触发告警
     *
     * @param alert        告警配置
     * @param currentValue 当前值
     * @return 是否触发告警
     */
    public boolean checkAlert(MonitorAlert alert, Double currentValue) {
        if (alert == null || currentValue == null) {
            return false;
        }

        Double threshold = alert.getThreshold();
        String operator = alert.getOperator();

        if (threshold == null || operator == null) {
            return false;
        }

        boolean triggered = switch (operator) {
            case ">" -> currentValue > threshold;
            case "<" -> currentValue < threshold;
            case ">=" -> currentValue >= threshold;
            case "<=" -> currentValue <= threshold;
            case "==" -> Math.abs(currentValue - threshold) < 0.0001;
            case "!=" -> Math.abs(currentValue - threshold) >= 0.0001;
            default -> {
                log.warn("Unknown operator: {}", operator);
                yield false;
            }
        };

        log.debug("Alert check - alert: {}, current: {}, threshold: {}, operator: {}, triggered: {}",
                alert.getAlertName(), currentValue, threshold, operator, triggered);

        return triggered;
    }

    /**
     * 评估告警并返回告警消息
     *
     * @param alert        告警配置
     * @param currentValue 当前值
     * @return 告警消息，如果未触发则返回null
     */
    public String evaluate(MonitorAlert alert, Double currentValue) {
        if (checkAlert(alert, currentValue)) {
            String message = alert.getMessage();
            if (message == null || message.isEmpty()) {
                message = String.format("Alert '%s' triggered: current value %.2f %s threshold %.2f",
                        alert.getAlertName(), currentValue, alert.getOperator(), alert.getThreshold());
            }
            return message;
        }
        return null;
    }
}
