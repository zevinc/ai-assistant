package com.aiassistant.modules.rule.infrastructure.mapper;

import com.aiassistant.modules.rule.domain.entity.Rule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 规则Mapper接口
 * 基于MyBatis-Plus的BaseMapper提供基础CRUD操作
 */
@Mapper
public interface RuleMapper extends BaseMapper<Rule> {

}
