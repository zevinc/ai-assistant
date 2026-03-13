package com.aiassistant.modules.feedback.application.service;

import com.aiassistant.common.event.DomainEventPublisher;
import com.aiassistant.common.event.SimpleDomainEvent;
import com.aiassistant.modules.feedback.domain.entity.Feedback;
import com.aiassistant.modules.feedback.domain.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 反馈应用服务
 * 提供反馈管理的业务用例
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackApplicationService {

    private final FeedbackRepository feedbackRepository;
    private final DomainEventPublisher eventPublisher;

    /**
     * 创建反馈
     *
     * @param sessionId    会话ID
     * @param agentId      Agent ID
     * @param userId       用户ID
     * @param messageId    消息ID
     * @param rating       评分
     * @param comment      评论
     * @param feedbackType 反馈类型
     * @return 创建的反馈
     */
    @Transactional
    public Feedback createFeedback(String sessionId, Long agentId, Long userId,
                                   String messageId, Integer rating, String comment, String feedbackType) {
        log.info("Creating feedback for agent: {}, session: {}", agentId, sessionId);

        Feedback feedback = Feedback.builder()
                .sessionId(sessionId)
                .agentId(agentId)
                .userId(userId)
                .messageId(messageId)
                .rating(rating)
                .comment(comment)
                .feedbackType(feedbackType)
                .status(0)
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);

        // 发布反馈创建事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(savedFeedback.getId()), "Feedback", "FeedbackCreated"));

        log.info("Feedback created successfully with id: {}", savedFeedback.getId());
        return savedFeedback;
    }

    /**
     * 根据ID获取反馈
     *
     * @param id 反馈ID
     * @return 反馈实体
     */
    public Feedback getFeedback(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found: " + id));
    }

    /**
     * 根据会话ID获取反馈列表
     *
     * @param sessionId 会话ID
     * @return 反馈列表
     */
    public List<Feedback> getFeedbacksBySessionId(String sessionId) {
        return feedbackRepository.findBySessionId(sessionId);
    }

    /**
     * 根据Agent ID获取反馈列表
     *
     * @param agentId Agent ID
     * @return 反馈列表
     */
    public List<Feedback> getFeedbacksByAgentId(Long agentId) {
        return feedbackRepository.findByAgentId(agentId);
    }

    /**
     * 根据用户ID获取反馈列表
     *
     * @param userId 用户ID
     * @return 反馈列表
     */
    public List<Feedback> getFeedbacksByUserId(Long userId) {
        return feedbackRepository.findByUserId(userId);
    }

    /**
     * 更新反馈
     *
     * @param id      反馈ID
     * @param rating  评分
     * @param comment 评论
     * @return 更新后的反馈
     */
    @Transactional
    public Feedback updateFeedback(Long id, Integer rating, String comment) {
        log.info("Updating feedback: {}", id);

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feedback not found: " + id));

        if (rating != null) {
            feedback.setRating(rating);
        }
        if (comment != null) {
            feedback.setComment(comment);
        }

        Feedback updatedFeedback = feedbackRepository.update(feedback);

        // 发布反馈更新事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(id), "Feedback", "FeedbackUpdated"));

        log.info("Feedback updated successfully: {}", id);
        return updatedFeedback;
    }

    /**
     * 删除反馈
     *
     * @param id 反馈ID
     */
    @Transactional
    public void deleteFeedback(Long id) {
        log.info("Deleting feedback: {}", id);

        feedbackRepository.deleteById(id);

        // 发布反馈删除事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(id), "Feedback", "FeedbackDeleted"));

        log.info("Feedback deleted successfully: {}", id);
    }

    /**
     * 获取Agent的反馈统计
     *
     * @param agentId Agent ID
     * @return 反馈统计
     */
    public FeedbackStats getFeedbackStats(Long agentId) {
        long totalCount = feedbackRepository.countByAgentId(agentId);
        Double averageRating = feedbackRepository.avgRatingByAgentId(agentId);

        return new FeedbackStats(agentId, totalCount, averageRating != null ? averageRating : 0.0);
    }

    /**
     * 反馈统计内部类
     */
    public record FeedbackStats(Long agentId, long totalCount, double averageRating) {}
}
