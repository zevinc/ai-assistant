package com.aiassistant.modules.feedback.infrastructure.repository;

import com.aiassistant.modules.feedback.domain.entity.Feedback;
import com.aiassistant.modules.feedback.domain.repository.FeedbackRepository;
import com.aiassistant.modules.feedback.infrastructure.mapper.FeedbackMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 反馈仓储实现
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class FeedbackRepositoryImpl implements FeedbackRepository {

    private final FeedbackMapper feedbackMapper;

    @Override
    public Optional<Feedback> findById(Long id) {
        return Optional.ofNullable(feedbackMapper.selectById(id));
    }

    @Override
    public List<Feedback> findBySessionId(String sessionId) {
        LambdaQueryWrapper<Feedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Feedback::getSessionId, sessionId)
                .orderByDesc(Feedback::getCreatedAt);
        return feedbackMapper.selectList(queryWrapper);
    }

    @Override
    public List<Feedback> findByAgentId(Long agentId) {
        LambdaQueryWrapper<Feedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Feedback::getAgentId, agentId)
                .orderByDesc(Feedback::getCreatedAt);
        return feedbackMapper.selectList(queryWrapper);
    }

    @Override
    public List<Feedback> findByUserId(Long userId) {
        LambdaQueryWrapper<Feedback> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Feedback::getUserId, userId)
                .orderByDesc(Feedback::getCreatedAt);
        return feedbackMapper.selectList(queryWrapper);
    }

    @Override
    public Feedback save(Feedback feedback) {
        feedbackMapper.insert(feedback);
        return feedback;
    }

    @Override
    public Feedback update(Feedback feedback) {
        feedbackMapper.updateById(feedback);
        return feedback;
    }

    @Override
    public void deleteById(Long id) {
        feedbackMapper.deleteById(id);
    }

    @Override
    public long countByAgentId(Long agentId) {
        return feedbackMapper.countByAgentId(agentId);
    }

    @Override
    public Double avgRatingByAgentId(Long agentId) {
        return feedbackMapper.avgRatingByAgentId(agentId);
    }
}
