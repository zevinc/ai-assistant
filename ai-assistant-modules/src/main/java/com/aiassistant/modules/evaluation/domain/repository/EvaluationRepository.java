package com.aiassistant.modules.evaluation.domain.repository;

import com.aiassistant.modules.evaluation.domain.entity.Evaluation;

import java.util.List;
import java.util.Optional;

/**
 * 评估仓储接口
 */
public interface EvaluationRepository {

    /**
     * 根据ID查找评估
     *
     * @param id 评估ID
     * @return 评估实体
     */
    Optional<Evaluation> findById(Long id);

    /**
     * 根据Agent ID查找评估列表
     *
     * @param agentId Agent ID
     * @return 评估列表
     */
    List<Evaluation> findByAgentId(Long agentId);

    /**
     * 根据会话ID查找评估列表
     *
     * @param sessionId 会话ID
     * @return 评估列表
     */
    List<Evaluation> findBySessionId(String sessionId);

    /**
     * 保存评估
     *
     * @param evaluation 评估实体
     * @return 保存后的评估
     */
    Evaluation save(Evaluation evaluation);

    /**
     * 批量保存评估
     *
     * @param evaluations 评估列表
     * @return 保存后的评估列表
     */
    List<Evaluation> saveBatch(List<Evaluation> evaluations);

    /**
     * 根据ID删除评估
     *
     * @param id 评估ID
     */
    void deleteById(Long id);
}
