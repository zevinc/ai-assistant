package com.aiassistant.modules.monitor.domain.repository;

import com.aiassistant.modules.monitor.domain.entity.MonitorAlert;

import java.util.List;
import java.util.Optional;

/**
 * 监控告警仓储接口
 */
public interface MonitorAlertRepository {

    /**
     * 根据ID查找告警
     *
     * @param id 告警ID
     * @return 告警实体
     */
    Optional<MonitorAlert> findById(Long id);

    /**
     * 查找活跃告警列表
     *
     * @return 活跃告警列表
     */
    List<MonitorAlert> findActiveAlerts();

    /**
     * 保存告警
     *
     * @param alert 告警实体
     * @return 保存后的告警
     */
    MonitorAlert save(MonitorAlert alert);

    /**
     * 更新告警
     *
     * @param alert 告警实体
     * @return 更新后的告警
     */
    MonitorAlert update(MonitorAlert alert);
}
