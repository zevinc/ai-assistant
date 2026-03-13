package com.aiassistant.modules.wiki.infrastructure.mapper;

import com.aiassistant.modules.wiki.domain.entity.WikiChunk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 知识库分块Mapper接口
 * 基于MyBatis-Plus的BaseMapper提供基础CRUD操作
 */
@Mapper
public interface WikiChunkMapper extends BaseMapper<WikiChunk> {

}
