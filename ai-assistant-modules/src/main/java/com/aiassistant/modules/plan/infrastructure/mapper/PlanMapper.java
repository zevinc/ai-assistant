package com.aiassistant.modules.plan.infrastructure.mapper;

import com.aiassistant.modules.plan.domain.entity.Plan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 计划Mapper接口
 */
@Mapper
public interface PlanMapper extends BaseMapper<Plan> {
}
