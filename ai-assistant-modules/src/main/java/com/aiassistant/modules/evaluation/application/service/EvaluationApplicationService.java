package com.aiassistant.modules.evaluation.application.service;

import com.aiassistant.common.event.DomainEventPublisher;
import com.aiassistant.common.event.SimpleDomainEvent;
import com.aiassistant.modules.evaluation.domain.entity.Evaluation;
import com.aiassistant.modules.evaluation.domain.repository.EvaluationRepository;
import com.aiassistant.modules.evaluation.domain.service.EvaluationEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 评估应用服务
 * 提供评估管理的业务用例
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EvaluationApplicationService {

    private final EvaluationRepository evaluationRepository;
    private final EvaluationEngine evaluationEngine;
    private final DomainEventPublisher eventPublisher;

    /**
     * 创建评估
     *
     * @param agentId         Agent ID
     * @param sessionId       会话ID
     * @param inputText       输入文本
     * @param outputText      输出文本
     * @param expectedOutput  期望输出
     * @param evaluationType  评估类型
     * @param metrics         评估指标
     * @return 创建的评估
     */
    @Transactional
    public Evaluation createEvaluation(Long agentId, String sessionId, String inputText,
                                       String outputText, String expectedOutput,
                                       String evaluationType, String metrics) {
        log.info("Creating evaluation for agent: {}", agentId);

        // 使用评估引擎计算评分
        double score = evaluationEngine.evaluate(inputText, outputText, expectedOutput);

        Evaluation evaluation = Evaluation.builder()
                .agentId(agentId)
                .sessionId(sessionId)
                .inputText(inputText)
                .outputText(outputText)
                .expectedOutput(expectedOutput)
                .score(score)
                .metrics(metrics)
                .evaluationType(evaluationType)
                .status(1) // 已完成
                .build();

        Evaluation savedEvaluation = evaluationRepository.save(evaluation);

        // 发布评估创建事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(savedEvaluation.getId()), "Evaluation", "evaluation.created"));

        log.info("Evaluation created successfully with id: {}, score: {}", savedEvaluation.getId(), score);
        return savedEvaluation;
    }

    /**
     * 批量创建评估
     *
     * @param requests 评估请求列表
     * @return 创建的评估列表
     */
    @Transactional
    public List<Evaluation> createEvaluationBatch(List<EvaluationRequest> requests) {
        log.info("Creating batch evaluation, count: {}", requests.size());

        List<Evaluation> evaluations = new ArrayList<>();

        for (EvaluationRequest request : requests) {
            double score = evaluationEngine.evaluate(
                    request.inputText(),
                    request.outputText(),
                    request.expectedOutput()
            );

            Evaluation evaluation = Evaluation.builder()
                    .agentId(request.agentId())
                    .sessionId(request.sessionId())
                    .inputText(request.inputText())
                    .outputText(request.outputText())
                    .expectedOutput(request.expectedOutput())
                    .score(score)
                    .metrics(request.metrics())
                    .evaluationType(request.evaluationType())
                    .status(1)
                    .build();

            evaluations.add(evaluation);
        }

        List<Evaluation> savedEvaluations = evaluationRepository.saveBatch(evaluations);

        // 发布批量评估完成事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(savedEvaluations.size()), "Evaluation", "evaluation.batch.completed"));

        log.info("Batch evaluation completed, count: {}", savedEvaluations.size());
        return savedEvaluations;
    }

    /**
     * 根据ID获取评估
     *
     * @param id 评估ID
     * @return 评估实体
     */
    public Evaluation getEvaluation(Long id) {
        return evaluationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evaluation not found: " + id));
    }

    /**
     * 根据Agent ID获取评估列表
     *
     * @param agentId Agent ID
     * @return 评估列表
     */
    public List<Evaluation> getEvaluationsByAgentId(Long agentId) {
        return evaluationRepository.findByAgentId(agentId);
    }

    /**
     * 根据会话ID获取评估列表
     *
     * @param sessionId 会话ID
     * @return 评估列表
     */
    public List<Evaluation> getEvaluationsBySessionId(String sessionId) {
        return evaluationRepository.findBySessionId(sessionId);
    }

    /**
     * 删除评估
     *
     * @param id 评估ID
     */
    @Transactional
    public void deleteEvaluation(Long id) {
        log.info("Deleting evaluation: {}", id);

        evaluationRepository.deleteById(id);

        // 发布评估删除事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(id), "Evaluation", "evaluation.deleted"));

        log.info("Evaluation deleted successfully: {}", id);
    }

    /**
     * 评估请求内部记录
     */
    public record EvaluationRequest(Long agentId, String sessionId, String inputText,
                                    String outputText, String expectedOutput,
                                    String evaluationType, String metrics) {}
}
