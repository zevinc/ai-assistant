package com.aiassistant.modules.agent.infrastructure.mapper;

import com.aiassistant.modules.agent.domain.entity.Agent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Agent Mapper接口
 */
@Mapper
public interface AgentMapper extends BaseMapper<Agent> {
}
