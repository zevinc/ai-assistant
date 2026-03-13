package com.aiassistant.modules.agent.infrastructure.repository;

import com.aiassistant.modules.agent.domain.entity.Agent;
import com.aiassistant.modules.agent.domain.repository.AgentRepository;
import com.aiassistant.modules.agent.infrastructure.mapper.AgentMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Agent仓储实现
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class AgentRepositoryImpl implements AgentRepository {

    private final AgentMapper agentMapper;

    @Override
    public Optional<Agent> findById(Long id) {
        return Optional.ofNullable(agentMapper.selectById(id));
    }

    @Override
    public Optional<Agent> findByName(String name) {
        LambdaQueryWrapper<Agent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Agent::getName, name);
        return Optional.ofNullable(agentMapper.selectOne(queryWrapper));
    }

    @Override
    public List<Agent> findBySpecId(Long specId) {
        LambdaQueryWrapper<Agent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Agent::getSpecId, specId)
                .orderByDesc(Agent::getCreatedAt);
        return agentMapper.selectList(queryWrapper);
    }

    @Override
    public List<Agent> listAll() {
        return agentMapper.selectList(null);
    }

    @Override
    public Agent save(Agent agent) {
        agentMapper.insert(agent);
        return agent;
    }

    @Override
    public Agent update(Agent agent) {
        agentMapper.updateById(agent);
        return agent;
    }

    @Override
    public void deleteById(Long id) {
        agentMapper.deleteById(id);
    }
}
