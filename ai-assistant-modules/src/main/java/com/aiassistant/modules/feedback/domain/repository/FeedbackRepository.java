package com.aiassistant.modules.feedback.domain.repository;

import com.aiassistant.modules.feedback.domain.entity.Feedback;

import java.util.List;
import java.util.Optional;

/**
 * 反馈仓储接口
 */
public interface FeedbackRepository {

    /**
     * 根据ID查找反馈
     *
     * @param id 反馈ID
     * @return 反馈实体
     */
    Optional<Feedback> findById(Long id);

    /**
     * 根据会话ID查找反馈列表
     *
     * @param sessionId 会话ID
     * @return 反馈列表
     */
    List<Feedback> findBySessionId(String sessionId);

    /**
     * 根据Agent ID查找反馈列表
     *
     * @param agentId Agent ID
     * @return 反馈列表
     */
    List<Feedback> findByAgentId(Long agentId);

    /**
     * 根据用户ID查找反馈列表
     *
     * @param userId 用户ID
     * @return 反馈列表
     */
    List<Feedback> findByUserId(Long userId);

    /**
     * 保存反馈
     *
     * @param feedback 反馈实体
     * @return 保存后的反馈
     */
    Feedback save(Feedback feedback);

    /**
     * 更新反馈
     *
     * @param feedback 反馈实体
     * @return 更新后的反馈
     */
    Feedback update(Feedback feedback);

    /**
     * 根据ID删除反馈
     *
     * @param id 反馈ID
     */
    void deleteById(Long id);

    /**
     * 统计Agent的反馈数量
     *
     * @param agentId Agent ID
     * @return 反馈数量
     */
    long countByAgentId(Long agentId);

    /**
     * 计算Agent的平均评分
     *
     * @param agentId Agent ID
     * @return 平均评分
     */
    Double avgRatingByAgentId(Long agentId);
}
