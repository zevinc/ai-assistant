package com.aiassistant.modules.agent.infrastructure.mapper;

import com.aiassistant.modules.agent.domain.entity.AgentSession;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Agent会话Mapper接口
 */
@Mapper
public interface AgentSessionMapper extends BaseMapper<AgentSession> {
}
