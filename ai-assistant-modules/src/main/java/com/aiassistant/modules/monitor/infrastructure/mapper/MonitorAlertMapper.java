package com.aiassistant.modules.monitor.infrastructure.mapper;

import com.aiassistant.modules.monitor.domain.entity.MonitorAlert;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 监控告警Mapper接口
 */
@Mapper
public interface MonitorAlertMapper extends BaseMapper<MonitorAlert> {
}
