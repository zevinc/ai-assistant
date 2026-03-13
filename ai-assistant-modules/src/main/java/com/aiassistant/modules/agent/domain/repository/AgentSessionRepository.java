package com.aiassistant.modules.agent.domain.repository;

import com.aiassistant.modules.agent.domain.entity.AgentSession;

import java.util.List;
import java.util.Optional;

/**
 * Agent会话仓储接口
 */
public interface AgentSessionRepository {

    /**
     * 根据ID查找会话
     *
     * @param id 会话ID
     * @return 会话实体
     */
    Optional<AgentSession> findById(Long id);

    /**
     * 根据会话ID查找会话
     *
     * @param sessionId 会话ID
     * @return 会话实体
     */
    Optional<AgentSession> findBySessionId(String sessionId);

    /**
     * 根据Agent ID查找会话列表
     *
     * @param agentId Agent ID
     * @return 会话列表
     */
    List<AgentSession> findByAgentId(Long agentId);

    /**
     * 根据用户ID查找会话列表
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    List<AgentSession> findByUserId(Long userId);

    /**
     * 保存会话
     *
     * @param session 会话实体
     * @return 保存后的会话
     */
    AgentSession save(AgentSession session);

    /**
     * 更新会话
     *
     * @param session 会话实体
     * @return 更新后的会话
     */
    AgentSession update(AgentSession session);
}
