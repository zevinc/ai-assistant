package com.aiassistant.modules.plan.infrastructure.repository;

import com.aiassistant.modules.plan.domain.entity.PlanStep;
import com.aiassistant.modules.plan.domain.repository.PlanStepRepository;
import com.aiassistant.modules.plan.infrastructure.mapper.PlanStepMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 计划步骤仓储实现
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class PlanStepRepositoryImpl implements PlanStepRepository {

    private final PlanStepMapper planStepMapper;

    @Override
    public Optional<PlanStep> findById(Long id) {
        return Optional.ofNullable(planStepMapper.selectById(id));
    }

    @Override
    public List<PlanStep> findByPlanId(Long planId) {
        LambdaQueryWrapper<PlanStep> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlanStep::getPlanId, planId)
                .orderByAsc(PlanStep::getStepIndex);
        return planStepMapper.selectList(queryWrapper);
    }

    @Override
    public Optional<PlanStep> findNextStep(Long planId) {
        LambdaQueryWrapper<PlanStep> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlanStep::getPlanId, planId)
                .eq(PlanStep::getStatus, "pending")
                .orderByAsc(PlanStep::getStepIndex)
                .last("LIMIT 1");
        return Optional.ofNullable(planStepMapper.selectOne(queryWrapper));
    }

    @Override
    public PlanStep save(PlanStep step) {
        planStepMapper.insert(step);
        return step;
    }

    @Override
    public PlanStep update(PlanStep step) {
        planStepMapper.updateById(step);
        return step;
    }

    @Override
    public List<PlanStep> saveBatch(List<PlanStep> steps) {
        for (PlanStep step : steps) {
            save(step);
        }
        return steps;
    }
}
