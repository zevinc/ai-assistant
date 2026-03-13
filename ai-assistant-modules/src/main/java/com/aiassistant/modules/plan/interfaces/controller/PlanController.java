package com.aiassistant.modules.plan.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.plan.application.service.PlanApplicationService;
import com.aiassistant.modules.plan.domain.entity.Plan;
import com.aiassistant.modules.plan.domain.entity.PlanStep;
import com.aiassistant.modules.plan.interfaces.dto.PlanCreateRequest;
import com.aiassistant.modules.plan.interfaces.dto.PlanStepUpdateRequest;
import com.aiassistant.modules.plan.interfaces.vo.PlanStepVO;
import com.aiassistant.modules.plan.interfaces.vo.PlanVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 计划控制器
 * 提供计划管理的REST API
 */
@RestController
@RequestMapping("/api/v1/plans")
@Slf4j
@RequiredArgsConstructor
public class PlanController {

    private final PlanApplicationService planApplicationService;

    /**
     * 创建计划
     *
     * @param request 计划创建请求
     * @return 创建的计划
     */
    @PostMapping
    public Result<PlanVO> create(@RequestBody PlanCreateRequest request) {
        log.info("Creating plan: {}", request.getName());

        Plan plan = planApplicationService.createPlan(
                request.getName(),
                request.getDescription(),
                request.getAgentId(),
                request.getSessionId(),
                request.getStrategy()
        );

        return Result.ok(toVO(plan));
    }

    /**
     * 获取计划详情
     *
     * @param id 计划ID
     * @return 计划详情
     */
    @GetMapping("/{id}")
    public Result<PlanVO> get(@PathVariable Long id) {
        log.info("Getting plan: {}", id);
        Plan plan = planApplicationService.getStatus(id);
        return Result.ok(toVO(plan));
    }

    /**
     * 执行计划
     *
     * @param id 计划ID
     * @return 更新后的计划
     */
    @PostMapping("/{id}/execute")
    public Result<PlanVO> execute(@PathVariable Long id) {
        log.info("Executing plan: {}", id);
        Plan plan = planApplicationService.executePlan(id);
        return Result.ok(toVO(plan));
    }

    /**
     * 暂停计划
     *
     * @param id 计划ID
     * @return 更新后的计划
     */
    @PostMapping("/{id}/pause")
    public Result<PlanVO> pause(@PathVariable Long id) {
        log.info("Pausing plan: {}", id);
        Plan plan = planApplicationService.pausePlan(id);
        return Result.ok(toVO(plan));
    }

    /**
     * 删除计划
     *
     * @param id 计划ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("Deleting plan: {}", id);
        // TODO: 实现删除逻辑
        return Result.ok();
    }

    /**
     * 获取计划的所有步骤
     *
     * @param id 计划ID
     * @return 步骤列表
     */
    @GetMapping("/{id}/steps")
    public Result<List<PlanStepVO>> getSteps(@PathVariable Long id) {
        log.info("Getting steps for plan: {}", id);
        List<PlanStep> steps = planApplicationService.listSteps(id);
        List<PlanStepVO> voList = steps.stream()
                .map(this::toStepVO)
                .collect(Collectors.toList());
        return Result.ok(voList);
    }

    /**
     * 更新步骤状态
     *
     * @param request 步骤更新请求
     * @return 更新后的步骤
     */
    @PutMapping("/steps")
    public Result<PlanStepVO> updateStep(@RequestBody PlanStepUpdateRequest request) {
        log.info("Updating step: {}", request.getStepId());
        PlanStep step = planApplicationService.updateStep(
                request.getStepId(),
                request.getStatus(),
                request.getOutput()
        );
        return Result.ok(toStepVO(step));
    }

    /**
     * 将实体转换为VO
     */
    private PlanVO toVO(Plan plan) {
        return PlanVO.builder()
                .id(plan.getId())
                .name(plan.getName())
                .description(plan.getDescription())
                .agentId(plan.getAgentId())
                .sessionId(plan.getSessionId())
                .strategy(plan.getStrategy())
                .status(plan.getStatus())
                .totalSteps(plan.getTotalSteps())
                .completedSteps(plan.getCompletedSteps())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }

    /**
     * 将步骤实体转换为VO
     */
    private PlanStepVO toStepVO(PlanStep step) {
        return PlanStepVO.builder()
                .id(step.getId())
                .planId(step.getPlanId())
                .stepIndex(step.getStepIndex())
                .name(step.getName())
                .description(step.getDescription())
                .action(step.getAction())
                .input(step.getInput())
                .output(step.getOutput())
                .status(step.getStatus())
                .dependsOn(step.getDependsOn())
                .createdAt(step.getCreatedAt())
                .updatedAt(step.getUpdatedAt())
                .build();
    }
}
