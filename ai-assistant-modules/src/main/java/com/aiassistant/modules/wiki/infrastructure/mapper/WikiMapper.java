package com.aiassistant.modules.wiki.infrastructure.mapper;

import com.aiassistant.modules.wiki.domain.entity.Wiki;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 知识库Mapper接口
 * 基于MyBatis-Plus的BaseMapper提供基础CRUD操作
 */
@Mapper
public interface WikiMapper extends BaseMapper<Wiki> {

}
