package com.aiassistant.modules.plan.domain.repository;

import com.aiassistant.modules.plan.domain.entity.PlanStep;

import java.util.List;
import java.util.Optional;

/**
 * 计划步骤仓储接口
 */
public interface PlanStepRepository {

    /**
     * 根据ID查找步骤
     *
     * @param id 步骤ID
     * @return 步骤实体
     */
    Optional<PlanStep> findById(Long id);

    /**
     * 根据计划ID查找所有步骤
     *
     * @param planId 计划ID
     * @return 步骤列表
     */
    List<PlanStep> findByPlanId(Long planId);

    /**
     * 查找计划的下一个待执行步骤
     *
     * @param planId 计划ID
     * @return 下一个步骤
     */
    Optional<PlanStep> findNextStep(Long planId);

    /**
     * 保存步骤
     *
     * @param step 步骤实体
     * @return 保存后的步骤
     */
    PlanStep save(PlanStep step);

    /**
     * 更新步骤
     *
     * @param step 步骤实体
     * @return 更新后的步骤
     */
    PlanStep update(PlanStep step);

    /**
     * 批量保存步骤
     *
     * @param steps 步骤列表
     * @return 保存后的步骤列表
     */
    List<PlanStep> saveBatch(List<PlanStep> steps);
}
