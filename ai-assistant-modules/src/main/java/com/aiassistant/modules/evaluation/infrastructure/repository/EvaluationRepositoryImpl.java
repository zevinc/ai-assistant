package com.aiassistant.modules.evaluation.infrastructure.repository;

import com.aiassistant.modules.evaluation.domain.entity.Evaluation;
import com.aiassistant.modules.evaluation.domain.repository.EvaluationRepository;
import com.aiassistant.modules.evaluation.infrastructure.mapper.EvaluationMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 评估仓储实现
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class EvaluationRepositoryImpl implements EvaluationRepository {

    private final EvaluationMapper evaluationMapper;

    @Override
    public Optional<Evaluation> findById(Long id) {
        return Optional.ofNullable(evaluationMapper.selectById(id));
    }

    @Override
    public List<Evaluation> findByAgentId(Long agentId) {
        LambdaQueryWrapper<Evaluation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Evaluation::getAgentId, agentId)
                .orderByDesc(Evaluation::getCreatedAt);
        return evaluationMapper.selectList(queryWrapper);
    }

    @Override
    public List<Evaluation> findBySessionId(String sessionId) {
        LambdaQueryWrapper<Evaluation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Evaluation::getSessionId, sessionId)
                .orderByDesc(Evaluation::getCreatedAt);
        return evaluationMapper.selectList(queryWrapper);
    }

    @Override
    public Evaluation save(Evaluation evaluation) {
        evaluationMapper.insert(evaluation);
        return evaluation;
    }

    @Override
    public List<Evaluation> saveBatch(List<Evaluation> evaluations) {
        for (Evaluation evaluation : evaluations) {
            evaluationMapper.insert(evaluation);
        }
        return evaluations;
    }

    @Override
    public void deleteById(Long id) {
        evaluationMapper.deleteById(id);
    }
}
