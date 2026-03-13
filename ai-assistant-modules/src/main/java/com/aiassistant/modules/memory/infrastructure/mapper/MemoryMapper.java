package com.aiassistant.modules.memory.infrastructure.mapper;

import com.aiassistant.modules.memory.domain.entity.Memory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 记忆Mapper接口
 */
@Mapper
public interface MemoryMapper extends BaseMapper<Memory> {
}
