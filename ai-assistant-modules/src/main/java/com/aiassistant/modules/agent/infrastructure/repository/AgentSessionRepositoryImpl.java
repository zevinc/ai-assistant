package com.aiassistant.modules.agent.infrastructure.repository;

import com.aiassistant.modules.agent.domain.entity.AgentSession;
import com.aiassistant.modules.agent.domain.repository.AgentSessionRepository;
import com.aiassistant.modules.agent.infrastructure.mapper.AgentSessionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Agent会话仓储实现
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class AgentSessionRepositoryImpl implements AgentSessionRepository {

    private final AgentSessionMapper agentSessionMapper;

    @Override
    public Optional<AgentSession> findById(Long id) {
        return Optional.ofNullable(agentSessionMapper.selectById(id));
    }

    @Override
    public Optional<AgentSession> findBySessionId(String sessionId) {
        LambdaQueryWrapper<AgentSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgentSession::getSessionId, sessionId);
        return Optional.ofNullable(agentSessionMapper.selectOne(queryWrapper));
    }

    @Override
    public List<AgentSession> findByAgentId(Long agentId) {
        LambdaQueryWrapper<AgentSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgentSession::getAgentId, agentId)
                .orderByDesc(AgentSession::getCreatedAt);
        return agentSessionMapper.selectList(queryWrapper);
    }

    @Override
    public List<AgentSession> findByUserId(Long userId) {
        LambdaQueryWrapper<AgentSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgentSession::getUserId, userId)
                .orderByDesc(AgentSession::getCreatedAt);
        return agentSessionMapper.selectList(queryWrapper);
    }

    @Override
    public AgentSession save(AgentSession session) {
        agentSessionMapper.insert(session);
        return session;
    }

    @Override
    public AgentSession update(AgentSession session) {
        agentSessionMapper.updateById(session);
        return session;
    }
}
