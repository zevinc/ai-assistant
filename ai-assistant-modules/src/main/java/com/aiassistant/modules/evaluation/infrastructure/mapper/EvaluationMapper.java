package com.aiassistant.modules.evaluation.infrastructure.mapper;

import com.aiassistant.modules.evaluation.domain.entity.Evaluation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评估Mapper接口
 */
@Mapper
public interface EvaluationMapper extends BaseMapper<Evaluation> {
}
