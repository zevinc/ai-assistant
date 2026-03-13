package com.aiassistant.modules.plan.infrastructure.mapper;

import com.aiassistant.modules.plan.domain.entity.PlanStep;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 计划步骤Mapper接口
 */
@Mapper
public interface PlanStepMapper extends BaseMapper<PlanStep> {
}
