package com.aiassistant.modules.spec.infrastructure.mapper;

import com.aiassistant.modules.spec.domain.entity.Spec;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 规格Mapper接口
 * 基于MyBatis-Plus的BaseMapper提供基础CRUD操作
 */
@Mapper
public interface SpecMapper extends BaseMapper<Spec> {

}
