package com.aiassistant.modules.command.infrastructure.mapper;

import com.aiassistant.modules.command.domain.entity.Command;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 命令Mapper接口
 */
@Mapper
public interface CommandMapper extends BaseMapper<Command> {
}
