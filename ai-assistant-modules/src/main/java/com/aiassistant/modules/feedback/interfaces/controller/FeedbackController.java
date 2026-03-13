package com.aiassistant.modules.feedback.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.feedback.application.service.FeedbackApplicationService;
import com.aiassistant.modules.feedback.domain.entity.Feedback;
import com.aiassistant.modules.feedback.interfaces.dto.FeedbackCreateRequest;
import com.aiassistant.modules.feedback.interfaces.vo.FeedbackStatsVO;
import com.aiassistant.modules.feedback.interfaces.vo.FeedbackVO;
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
 * 反馈控制器
 * 提供反馈管理的REST API
 */
@RestController
@RequestMapping("/api/v1/feedbacks")
@Slf4j
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackApplicationService feedbackApplicationService;

    /**
     * 创建反馈
     *
     * @param request 反馈创建请求
     * @return 创建的反馈
     */
    @PostMapping
    public Result<FeedbackVO> create(@RequestBody FeedbackCreateRequest request) {
        log.info("Creating feedback for agent: {}", request.getAgentId());

        Feedback feedback = feedbackApplicationService.createFeedback(
                request.getSessionId(),
                request.getAgentId(),
                request.getUserId(),
                request.getMessageId(),
                request.getRating(),
                request.getComment(),
                request.getFeedbackType()
        );

        return Result.ok(toVO(feedback));
    }

    /**
     * 获取反馈详情
     *
     * @param id 反馈ID
     * @return 反馈详情
     */
    @GetMapping("/{id}")
    public Result<FeedbackVO> get(@PathVariable Long id) {
        log.info("Getting feedback: {}", id);
        Feedback feedback = feedbackApplicationService.getFeedback(id);
        return Result.ok(toVO(feedback));
    }

    /**
     * 根据会话ID获取反馈列表
     *
     * @param sessionId 会话ID
     * @return 反馈列表
     */
    @GetMapping("/session/{sessionId}")
    public Result<List<FeedbackVO>> getBySessionId(@PathVariable String sessionId) {
        log.info("Getting feedbacks by session: {}", sessionId);
        List<Feedback> feedbacks = feedbackApplicationService.getFeedbacksBySessionId(sessionId);
        List<FeedbackVO> voList = feedbacks.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(voList);
    }

    /**
     * 根据Agent ID获取反馈列表
     *
     * @param agentId Agent ID
     * @return 反馈列表
     */
    @GetMapping("/agent/{agentId}")
    public Result<List<FeedbackVO>> getByAgentId(@PathVariable Long agentId) {
        log.info("Getting feedbacks by agent: {}", agentId);
        List<Feedback> feedbacks = feedbackApplicationService.getFeedbacksByAgentId(agentId);
        List<FeedbackVO> voList = feedbacks.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(voList);
    }

    /**
     * 根据用户ID获取反馈列表
     *
     * @param userId 用户ID
     * @return 反馈列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<FeedbackVO>> getByUserId(@PathVariable Long userId) {
        log.info("Getting feedbacks by user: {}", userId);
        List<Feedback> feedbacks = feedbackApplicationService.getFeedbacksByUserId(userId);
        List<FeedbackVO> voList = feedbacks.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(voList);
    }

    /**
     * 更新反馈
     *
     * @param id      反馈ID
     * @param request 反馈更新请求
     * @return 更新后的反馈
     */
    @PutMapping("/{id}")
    public Result<FeedbackVO> update(@PathVariable Long id, @RequestBody FeedbackCreateRequest request) {
        log.info("Updating feedback: {}", id);

        Feedback feedback = feedbackApplicationService.updateFeedback(
                id,
                request.getRating(),
                request.getComment()
        );

        return Result.ok(toVO(feedback));
    }

    /**
     * 删除反馈
     *
     * @param id 反馈ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("Deleting feedback: {}", id);
        feedbackApplicationService.deleteFeedback(id);
        return Result.ok();
    }

    /**
     * 获取Agent的反馈统计
     *
     * @param agentId Agent ID
     * @return 反馈统计
     */
    @GetMapping("/stats/agent/{agentId}")
    public Result<FeedbackStatsVO> getStats(@PathVariable Long agentId) {
        log.info("Getting feedback stats for agent: {}", agentId);

        FeedbackApplicationService.FeedbackStats stats = feedbackApplicationService.getFeedbackStats(agentId);

        FeedbackStatsVO statsVO = FeedbackStatsVO.builder()
                .agentId(stats.agentId())
                .totalCount(stats.totalCount())
                .averageRating(stats.averageRating())
                .build();

        return Result.ok(statsVO);
    }

    /**
     * 将实体转换为VO
     */
    private FeedbackVO toVO(Feedback feedback) {
        return FeedbackVO.builder()
                .id(feedback.getId())
                .sessionId(feedback.getSessionId())
                .agentId(feedback.getAgentId())
                .userId(feedback.getUserId())
                .messageId(feedback.getMessageId())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .feedbackType(feedback.getFeedbackType())
                .status(feedback.getStatus())
                .createdAt(feedback.getCreatedAt())
                .updatedAt(feedback.getUpdatedAt())
                .build();
    }
}
