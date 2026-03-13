package com.aiassistant.modules.plan.infrastructure.repository;

import com.aiassistant.modules.plan.domain.entity.Plan;
import com.aiassistant.modules.plan.domain.repository.PlanRepository;
import com.aiassistant.modules.plan.infrastructure.mapper.PlanMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 计划仓储实现
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class PlanRepositoryImpl implements PlanRepository {

    private final PlanMapper planMapper;

    @Override
    public Optional<Plan> findById(Long id) {
        return Optional.ofNullable(planMapper.selectById(id));
    }

    @Override
    public List<Plan> findByAgentId(Long agentId) {
        LambdaQueryWrapper<Plan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Plan::getAgentId, agentId)
                .orderByDesc(Plan::getCreatedAt);
        return planMapper.selectList(queryWrapper);
    }

    @Override
    public List<Plan> findBySessionId(String sessionId) {
        LambdaQueryWrapper<Plan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Plan::getSessionId, sessionId)
                .orderByDesc(Plan::getCreatedAt);
        return planMapper.selectList(queryWrapper);
    }

    @Override
    public Plan save(Plan plan) {
        planMapper.insert(plan);
        return plan;
    }

    @Override
    public Plan update(Plan plan) {
        planMapper.updateById(plan);
        return plan;
    }

    @Override
    public void deleteById(Long id) {
        planMapper.deleteById(id);
    }
}
