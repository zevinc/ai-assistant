package com.aiassistant.modules.rule.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.rule.application.service.RuleApplicationService;
import com.aiassistant.modules.rule.domain.entity.Rule;
import com.aiassistant.modules.rule.domain.enums.RuleTypeEnum;
import com.aiassistant.modules.rule.interfaces.dto.RuleCreateRequest;
import com.aiassistant.modules.rule.interfaces.dto.RuleEvaluateRequest;
import com.aiassistant.modules.rule.interfaces.vo.RuleEvaluationVO;
import com.aiassistant.modules.rule.interfaces.vo.RuleVO;
import com.aiassistant.modules.rule.domain.service.RuleEvaluator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 规则控制器
 * 提供规则管理的REST API
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/rules")
@RequiredArgsConstructor
public class RuleController {

    private final RuleApplicationService ruleApplicationService;

    /**
     * 创建规则
     *
     * @param request 创建请求
     * @return 创建后的规则
     */
    @PostMapping
    public Result<RuleVO> create(@Valid @RequestBody RuleCreateRequest request) {
        log.info("创建规则请求: {}", request.getName());

        Rule rule = Rule.builder()
                .name(request.getName())
                .description(request.getDescription())
                .ruleType(request.getRuleType())
                .expression(request.getExpression())
                .priority(request.getPriority())
                .specId(request.getSpecId())
                .build();

        Rule createdRule = ruleApplicationService.create(rule);
        return Result.ok(toVO(createdRule));
    }

    /**
     * 更新规则
     *
     * @param id 规则ID
     * @param request 更新请求
     * @return 更新后的规则
     */
    @PutMapping("/{id}")
    public Result<RuleVO> update(@PathVariable Long id, @Valid @RequestBody RuleCreateRequest request) {
        log.info("更新规则请求: ID={}", id);

        Rule rule = Rule.builder()
                .name(request.getName())
                .description(request.getDescription())
                .ruleType(request.getRuleType())
                .expression(request.getExpression())
                .priority(request.getPriority())
                .specId(request.getSpecId())
                .build();
        rule.setId(id);

        Rule updatedRule = ruleApplicationService.update(rule);
        return Result.ok(toVO(updatedRule));
    }

    /**
     * 根据ID查询规则
     *
     * @param id 规则ID
     * @return 规则详情
     */
    @GetMapping("/{id}")
    public Result<RuleVO> getById(@PathVariable Long id) {
        log.info("查询规则: ID={}", id);

        return ruleApplicationService.findById(id)
                .map(rule -> Result.ok(toVO(rule)))
                .orElse(Result.fail(404, "规则不存在"));
    }

    /**
     * 根据规格ID查询规则列表
     *
     * @param specId 规格ID
     * @return 规则列表
     */
    @GetMapping("/spec/{specId}")
    public Result<List<RuleVO>> listBySpecId(@PathVariable Long specId) {
        log.info("根据规格ID查询规则: specId={}", specId);

        List<RuleVO> ruleVOList = ruleApplicationService.findBySpecId(specId).stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(ruleVOList);
    }

    /**
     * 根据类型查询规则列表
     *
     * @param ruleType 规则类型
     * @return 规则列表
     */
    @GetMapping("/type/{ruleType}")
    public Result<List<RuleVO>> listByType(@PathVariable String ruleType) {
        log.info("根据类型查询规则: type={}", ruleType);

        List<RuleVO> ruleVOList = ruleApplicationService.listByType(ruleType).stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(ruleVOList);
    }

    /**
     * 查询所有规则
     *
     * @return 规则列表
     */
    @GetMapping
    public Result<List<RuleVO>> listAll() {
        log.info("查询所有规则");

        List<RuleVO> ruleVOList = ruleApplicationService.listAll().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(ruleVOList);
    }

    /**
     * 删除规则
     *
     * @param id 规则ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("删除规则: ID={}", id);

        ruleApplicationService.deleteById(id);
        return Result.ok();
    }

    /**
     * 评估规则
     *
     * @param id 规则ID
     * @param request 评估请求
     * @return 评估结果
     */
    @PostMapping("/{id}/evaluate")
    public Result<RuleEvaluationVO> evaluate(
            @PathVariable Long id,
            @RequestBody RuleEvaluateRequest request) {
        log.info("评估规则: ID={}", id);

        RuleEvaluator.EvaluationResult result =
                ruleApplicationService.evaluateWithDetails(id, request.getContext());

        RuleEvaluationVO vo = RuleEvaluationVO.builder()
                .ruleId(result.getRuleId())
                .ruleName(result.getRuleName())
                .expression(result.getExpression())
                .success(result.isSuccess())
                .message(result.getMessage())
                .error(result.getError())
                .build();

        return Result.ok(vo);
    }

    /**
     * 批量评估规则
     *
     * @param specId 规格ID（可选）
     * @param ruleType 规则类型（可选）
     * @param request 评估请求
     * @return 评估结果列表
     */
    @PostMapping("/batch-evaluate")
    public Result<List<RuleEvaluationVO>> batchEvaluate(
            @RequestParam(required = false) Long specId,
            @RequestParam(required = false) String ruleType,
            @RequestBody RuleEvaluateRequest request) {
        log.info("批量评估规则: specId={}, ruleType={}", specId, ruleType);

        List<RuleEvaluator.EvaluationResult> results =
                ruleApplicationService.batchEvaluate(specId, ruleType, request.getContext());

        List<RuleEvaluationVO> voList = results.stream()
                .map(result -> RuleEvaluationVO.builder()
                        .ruleId(result.getRuleId())
                        .ruleName(result.getRuleName())
                        .expression(result.getExpression())
                        .success(result.isSuccess())
                        .message(result.getMessage())
                        .error(result.getError())
                        .build())
                .collect(Collectors.toList());

        return Result.ok(voList);
    }

    /**
     * 启用规则
     *
     * @param id 规则ID
     * @return 更新后的规则
     */
    @PostMapping("/{id}/enable")
    public Result<RuleVO> enable(@PathVariable Long id) {
        log.info("启用规则: ID={}", id);

        Rule rule = ruleApplicationService.enable(id);
        return Result.ok(toVO(rule));
    }

    /**
     * 禁用规则
     *
     * @param id 规则ID
     * @return 更新后的规则
     */
    @PostMapping("/{id}/disable")
    public Result<RuleVO> disable(@PathVariable Long id) {
        log.info("禁用规则: ID={}", id);

        Rule rule = ruleApplicationService.disable(id);
        return Result.ok(toVO(rule));
    }

    /**
     * 将实体转换为VO
     *
     * @param rule 规则实体
     * @return 规则VO
     */
    private RuleVO toVO(Rule rule) {
        RuleTypeEnum typeEnum = RuleTypeEnum.getByCode(rule.getRuleType());

        return RuleVO.builder()
                .id(rule.getId())
                .name(rule.getName())
                .description(rule.getDescription())
                .ruleType(rule.getRuleType())
                .ruleTypeDesc(typeEnum != null ? typeEnum.getDescription() : null)
                .expression(rule.getExpression())
                .priority(rule.getPriority())
                .specId(rule.getSpecId())
                .status(rule.getStatus())
                .statusDesc(rule.getStatus() != null && rule.getStatus() == 1 ? "启用" : "禁用")
                .createdAt(rule.getCreatedAt())
                .updatedAt(rule.getUpdatedAt())
                .build();
    }
}
