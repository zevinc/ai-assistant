package com.aiassistant.modules.command.infrastructure.mapper;

import com.aiassistant.modules.command.domain.entity.CommandExecution;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 命令执行Mapper接口
 */
@Mapper
public interface CommandExecutionMapper extends BaseMapper<CommandExecution> {
}
