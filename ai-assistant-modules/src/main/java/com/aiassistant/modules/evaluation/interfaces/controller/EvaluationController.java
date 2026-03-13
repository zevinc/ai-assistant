package com.aiassistant.modules.evaluation.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.evaluation.application.service.EvaluationApplicationService;
import com.aiassistant.modules.evaluation.domain.entity.Evaluation;
import com.aiassistant.modules.evaluation.interfaces.dto.EvaluationBatchRequest;
import com.aiassistant.modules.evaluation.interfaces.dto.EvaluationCreateRequest;
import com.aiassistant.modules.evaluation.interfaces.vo.EvaluationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评估控制器
 * 提供评估管理的REST API
 */
@RestController
@RequestMapping("/api/v1/evaluations")
@Slf4j
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationApplicationService evaluationApplicationService;

    /**
     * 创建评估
     *
     * @param request 评估创建请求
     * @return 创建的评估
     */
    @PostMapping
    public Result<EvaluationVO> create(@RequestBody EvaluationCreateRequest request) {
        log.info("Creating evaluation for agent: {}", request.getAgentId());

        Evaluation evaluation = evaluationApplicationService.createEvaluation(
                request.getAgentId(),
                request.getSessionId(),
                request.getInputText(),
                request.getOutputText(),
                request.getExpectedOutput(),
                request.getEvaluationType(),
                request.getMetrics()
        );

        return Result.ok(toVO(evaluation));
    }

    /**
     * 批量创建评估
     *
     * @param request 批量评估请求
     * @return 创建的评估列表
     */
    @PostMapping("/batch")
    public Result<List<EvaluationVO>> createBatch(@RequestBody EvaluationBatchRequest request) {
        log.info("Creating batch evaluation, count: {}", request.getItems().size());

        List<EvaluationApplicationService.EvaluationRequest> evaluationRequests = request.getItems().stream()
                .map(item -> new EvaluationApplicationService.EvaluationRequest(
                        item.getAgentId(),
                        item.getSessionId(),
                        item.getInputText(),
                        item.getOutputText(),
                        item.getExpectedOutput(),
                        item.getEvaluationType(),
                        item.getMetrics()
                ))
                .collect(Collectors.toList());

        List<Evaluation> evaluations = evaluationApplicationService.createEvaluationBatch(evaluationRequests);

        List<EvaluationVO> voList = evaluations.stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return Result.ok(voList);
    }

    /**
     * 获取评估详情
     *
     * @param id 评估ID
     * @return 评估详情
     */
    @GetMapping("/{id}")
    public Result<EvaluationVO> get(@PathVariable Long id) {
        log.info("Getting evaluation: {}", id);
        Evaluation evaluation = evaluationApplicationService.getEvaluation(id);
        return Result.ok(toVO(evaluation));
    }

    /**
     * 根据Agent ID获取评估列表
     *
     * @param agentId Agent ID
     * @return 评估列表
     */
    @GetMapping("/agent/{agentId}")
    public Result<List<EvaluationVO>> getByAgentId(@PathVariable Long agentId) {
        log.info("Getting evaluations by agent: {}", agentId);
        List<Evaluation> evaluations = evaluationApplicationService.getEvaluationsByAgentId(agentId);
        List<EvaluationVO> voList = evaluations.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(voList);
    }

    /**
     * 根据会话ID获取评估列表
     *
     * @param sessionId 会话ID
     * @return 评估列表
     */
    @GetMapping("/session/{sessionId}")
    public Result<List<EvaluationVO>> getBySessionId(@PathVariable String sessionId) {
        log.info("Getting evaluations by session: {}", sessionId);
        List<Evaluation> evaluations = evaluationApplicationService.getEvaluationsBySessionId(sessionId);
        List<EvaluationVO> voList = evaluations.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(voList);
    }

    /**
     * 删除评估
     *
     * @param id 评估ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("Deleting evaluation: {}", id);
        evaluationApplicationService.deleteEvaluation(id);
        return Result.ok();
    }

    /**
     * 将实体转换为VO
     */
    private EvaluationVO toVO(Evaluation evaluation) {
        return EvaluationVO.builder()
                .id(evaluation.getId())
                .agentId(evaluation.getAgentId())
                .sessionId(evaluation.getSessionId())
                .inputText(evaluation.getInputText())
                .outputText(evaluation.getOutputText())
                .expectedOutput(evaluation.getExpectedOutput())
                .score(evaluation.getScore())
                .metrics(evaluation.getMetrics())
                .evaluationType(evaluation.getEvaluationType())
                .status(evaluation.getStatus())
                .createdAt(evaluation.getCreatedAt())
                .updatedAt(evaluation.getUpdatedAt())
                .build();
    }
}
