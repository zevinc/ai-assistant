package com.aiassistant.modules.monitor.infrastructure.repository;

import com.aiassistant.modules.monitor.domain.entity.MonitorAlert;
import com.aiassistant.modules.monitor.domain.enums.AlertStatusEnum;
import com.aiassistant.modules.monitor.domain.repository.MonitorAlertRepository;
import com.aiassistant.modules.monitor.infrastructure.mapper.MonitorAlertMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 监控告警仓储实现
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class MonitorAlertRepositoryImpl implements MonitorAlertRepository {

    private final MonitorAlertMapper monitorAlertMapper;

    @Override
    public Optional<MonitorAlert> findById(Long id) {
        return Optional.ofNullable(monitorAlertMapper.selectById(id));
    }

    @Override
    public List<MonitorAlert> findActiveAlerts() {
        LambdaQueryWrapper<MonitorAlert> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MonitorAlert::getStatus, AlertStatusEnum.ACTIVE.getCode())
                .orderByDesc(MonitorAlert::getCreatedAt);
        return monitorAlertMapper.selectList(queryWrapper);
    }

    @Override
    public MonitorAlert save(MonitorAlert alert) {
        monitorAlertMapper.insert(alert);
        return alert;
    }

    @Override
    public MonitorAlert update(MonitorAlert alert) {
        monitorAlertMapper.updateById(alert);
        return alert;
    }
}
