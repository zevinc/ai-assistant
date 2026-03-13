package com.aiassistant.modules.monitor.infrastructure.mapper;

import com.aiassistant.modules.monitor.domain.entity.MonitorMetric;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 监控指标Mapper接口
 */
@Mapper
public interface MonitorMetricMapper extends BaseMapper<MonitorMetric> {
}
