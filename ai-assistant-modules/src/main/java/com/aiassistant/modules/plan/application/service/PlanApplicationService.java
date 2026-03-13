package com.aiassistant.modules.plan.application.service;

import com.aiassistant.common.event.DomainEventPublisher;
import com.aiassistant.common.event.SimpleDomainEvent;
import com.aiassistant.modules.plan.domain.entity.Plan;
import com.aiassistant.modules.plan.domain.entity.PlanStep;
import com.aiassistant.modules.plan.domain.enums.PlanStatusEnum;
import com.aiassistant.modules.plan.domain.repository.PlanRepository;
import com.aiassistant.modules.plan.domain.repository.PlanStepRepository;
import com.aiassistant.modules.plan.domain.service.PlanGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 计划应用服务
 * 提供计划管理的业务用例
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PlanApplicationService {

    private final PlanRepository planRepository;
    private final PlanStepRepository planStepRepository;
    private final PlanGenerator planGenerator;
    private final DomainEventPublisher eventPublisher;

    /**
     * 创建计划
     *
     * @param name        计划名称
     * @param description 计划描述
     * @param agentId     Agent ID
     * @param sessionId   会话ID
     * @param strategy    执行策略
     * @return 创建的计划
     */
    @Transactional
    public Plan createPlan(String name, String description, Long agentId, String sessionId, String strategy) {
        log.info("Creating plan: {} for agent: {}", name, agentId);

        // 使用计划生成器创建计划
        Plan plan = planGenerator.generatePlan(description, strategy);
        plan.setName(name);
        plan.setAgentId(agentId);
        plan.setSessionId(sessionId);

        // 保存计划
        Plan savedPlan = planRepository.save(plan);

        // 生成并保存步骤
        List<PlanStep> steps = planGenerator.generateSteps(savedPlan);
        if (!steps.isEmpty()) {
            steps = planStepRepository.saveBatch(steps);
            savedPlan.setTotalSteps(steps.size());
            savedPlan = planRepository.update(savedPlan);
        }

        // 发布计划创建事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(savedPlan.getId()), "Plan", "PlanCreated"));

        log.info("Plan created successfully with id: {}, steps: {}", savedPlan.getId(), steps.size());
        return savedPlan;
    }

    /**
     * 执行计划
     *
     * @param planId 计划ID
     * @return 更新后的计划
     */
    @Transactional
    public Plan executePlan(Long planId) {
        log.info("Executing plan: {}", planId);

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));

        plan.setStatus(PlanStatusEnum.RUNNING.getCode());
        Plan updatedPlan = planRepository.update(plan);

        // 发布计划执行事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(planId), "Plan", "PlanExecutionStarted"));

        log.info("Plan execution started: {}", planId);
        return updatedPlan;
    }

    /**
     * 暂停计划
     *
     * @param planId 计划ID
     * @return 更新后的计划
     */
    @Transactional
    public Plan pausePlan(Long planId) {
        log.info("Pausing plan: {}", planId);

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));

        plan.setStatus(PlanStatusEnum.PAUSED.getCode());
        Plan updatedPlan = planRepository.update(plan);

        // 发布计划暂停事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(planId), "Plan", "PlanPaused"));

        log.info("Plan paused: {}", planId);
        return updatedPlan;
    }

    /**
     * 获取计划状态
     *
     * @param planId 计划ID
     * @return 计划实体
     */
    public Plan getStatus(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));
    }

    /**
     * 获取计划的所有步骤
     *
     * @param planId 计划ID
     * @return 步骤列表
     */
    public List<PlanStep> listSteps(Long planId) {
        return planStepRepository.findByPlanId(planId);
    }

    /**
     * 更新步骤状态
     *
     * @param stepId 步骤ID
     * @param status 状态
     * @param output 输出结果
     * @return 更新后的步骤
     */
    @Transactional
    public PlanStep updateStep(Long stepId, String status, String output) {
        log.info("Updating step: {} to status: {}", stepId, status);

        PlanStep step = planStepRepository.findById(stepId)
                .orElseThrow(() -> new IllegalArgumentException("Step not found: " + stepId));

        step.setStatus(status);
        step.setOutput(output);
        PlanStep updatedStep = planStepRepository.update(step);

        // 如果步骤完成，更新计划的完成步骤数
        if ("completed".equals(status)) {
            Plan plan = planRepository.findById(step.getPlanId()).orElse(null);
            if (plan != null) {
                int completedSteps = plan.getCompletedSteps() != null ? plan.getCompletedSteps() : 0;
                plan.setCompletedSteps(completedSteps + 1);

                // 检查是否所有步骤都已完成
                if (plan.getCompletedSteps().equals(plan.getTotalSteps())) {
                    plan.setStatus(PlanStatusEnum.COMPLETED.getCode());
                    eventPublisher.publish(new SimpleDomainEvent(String.valueOf(plan.getId()), "Plan", "PlanCompleted"));
                }

                planRepository.update(plan);
            }
        }

        return updatedStep;
    }
}
