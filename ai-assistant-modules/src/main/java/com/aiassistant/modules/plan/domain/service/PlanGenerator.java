package com.aiassistant.modules.plan.domain.service;

import com.aiassistant.modules.plan.domain.entity.Plan;
import com.aiassistant.modules.plan.domain.entity.PlanStep;
import com.aiassistant.modules.plan.domain.enums.PlanStatusEnum;
import com.aiassistant.modules.plan.domain.enums.PlanStrategyEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 计划生成服务
 * 根据目标和策略生成执行计划
 */
@Service
public class PlanGenerator {

    /**
     * 根据目标和策略生成计划
     *
     * @param goal     目标描述
     * @param strategy 执行策略
     * @return 生成的计划
     */
    public Plan generatePlan(String goal, String strategy) {
        // 创建计划实体
        Plan plan = Plan.builder()
                .name("计划: " + truncate(goal, 50))
                .description(goal)
                .strategy(strategy)
                .status(PlanStatusEnum.CREATED.getCode())
                .totalSteps(0)
                .completedSteps(0)
                .build();

        return plan;
    }

    /**
     * 根据计划生成步骤
     *
     * @param plan 计划实体
     * @return 步骤列表
     */
    public List<PlanStep> generateSteps(Plan plan) {
        List<PlanStep> steps = new ArrayList<>();
        String strategy = plan.getStrategy();

        if (PlanStrategyEnum.REACT.getCode().equals(strategy)) {
            steps = generateReactSteps(plan);
        } else if (PlanStrategyEnum.PLAN_EXECUTE.getCode().equals(strategy)) {
            steps = generatePlanExecuteSteps(plan);
        } else if (PlanStrategyEnum.HIERARCHICAL.getCode().equals(strategy)) {
            steps = generateHierarchicalSteps(plan);
        }

        return steps;
    }

    /**
     * 生成ReAct策略的步骤
     */
    private List<PlanStep> generateReactSteps(Plan plan) {
        List<PlanStep> steps = new ArrayList<>();

        // ReAct策略：思考 -> 行动 -> 观察 循环
        steps.add(PlanStep.builder()
                .planId(plan.getId())
                .stepIndex(1)
                .name("分析目标")
                .description("分析用户目标，确定需要执行的动作")
                .action("think")
                .status("pending")
                .build());

        steps.add(PlanStep.builder()
                .planId(plan.getId())
                .stepIndex(2)
                .name("执行动作")
                .description("根据分析结果执行相应动作")
                .action("act")
                .status("pending")
                .dependsOn("1")
                .build());

        steps.add(PlanStep.builder()
                .planId(plan.getId())
                .stepIndex(3)
                .name("观察结果")
                .description("观察动作执行结果，决定下一步")
                .action("observe")
                .status("pending")
                .dependsOn("2")
                .build());

        return steps;
    }

    /**
     * 生成计划-执行策略的步骤
     */
    private List<PlanStep> generatePlanExecuteSteps(Plan plan) {
        List<PlanStep> steps = new ArrayList<>();

        // 计划-执行策略：先制定完整计划，再逐步执行
        steps.add(PlanStep.builder()
                .planId(plan.getId())
                .stepIndex(1)
                .name("制定计划")
                .description("根据目标制定详细的执行计划")
                .action("plan")
                .status("pending")
                .build());

        steps.add(PlanStep.builder()
                .planId(plan.getId())
                .stepIndex(2)
                .name("执行计划")
                .description("按照计划逐步执行各个步骤")
                .action("execute")
                .status("pending")
                .dependsOn("1")
                .build());

        steps.add(PlanStep.builder()
                .planId(plan.getId())
                .stepIndex(3)
                .name("验证结果")
                .description("验证执行结果是否符合预期")
                .action("verify")
                .status("pending")
                .dependsOn("2")
                .build());

        return steps;
    }

    /**
     * 生成层次化策略的步骤
     */
    private List<PlanStep> generateHierarchicalSteps(Plan plan) {
        List<PlanStep> steps = new ArrayList<>();

        // 层次化策略：将目标分解为子目标
        steps.add(PlanStep.builder()
                .planId(plan.getId())
                .stepIndex(1)
                .name("分解目标")
                .description("将主要目标分解为多个子目标")
                .action("decompose")
                .status("pending")
                .build());

        steps.add(PlanStep.builder()
                .planId(plan.getId())
                .stepIndex(2)
                .name("执行子目标1")
                .description("执行第一个子目标")
                .action("execute_subgoal")
                .status("pending")
                .dependsOn("1")
                .build());

        steps.add(PlanStep.builder()
                .planId(plan.getId())
                .stepIndex(3)
                .name("执行子目标2")
                .description("执行第二个子目标")
                .action("execute_subgoal")
                .status("pending")
                .dependsOn("2")
                .build());

        steps.add(PlanStep.builder()
                .planId(plan.getId())
                .stepIndex(4)
                .name("整合结果")
                .description("整合所有子目标的执行结果")
                .action("integrate")
                .status("pending")
                .dependsOn("3")
                .build());

        return steps;
    }

    /**
     * 截断字符串
     */
    private String truncate(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        return str.length() > maxLength ? str.substring(0, maxLength) + "..." : str;
    }
}
