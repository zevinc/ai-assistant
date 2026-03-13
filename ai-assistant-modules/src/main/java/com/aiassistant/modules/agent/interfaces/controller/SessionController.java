package com.aiassistant.modules.agent.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.agent.application.service.AgentApplicationService;
import com.aiassistant.modules.agent.domain.entity.AgentSession;
import com.aiassistant.modules.agent.interfaces.vo.AgentSessionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 会话控制器
 * 提供会话管理的REST API
 */
@RestController
@RequestMapping("/api/v1/sessions")
@Slf4j
@RequiredArgsConstructor
public class SessionController {

    private final AgentApplicationService agentApplicationService;

    /**
     * 获取Agent的所有会话
     *
     * @param agentId Agent ID
     * @return 会话列表
     */
    @GetMapping
    public Result<List<AgentSessionVO>> list(@RequestParam Long agentId) {
        log.info("Listing sessions for agent: {}", agentId);
        List<AgentSession> sessions = agentApplicationService.listSessions(agentId);
        List<AgentSessionVO> voList = sessions.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(voList);
    }

    /**
     * 获取会话详情
     *
     * @param sessionId 会话ID
     * @return 会话详情
     */
    @GetMapping("/{sessionId}")
    public Result<AgentSessionVO> get(@PathVariable String sessionId) {
        log.info("Getting session: {}", sessionId);
        // TODO: 实现获取单个会话的方法
        return Result.ok();
    }

    /**
     * 创建新会话
     *
     * @param agentId Agent ID
     * @param userId  用户ID（可选）
     * @return 创建的会话
     */
    @PostMapping
    public Result<AgentSessionVO> create(@RequestParam Long agentId,
                                         @RequestParam(required = false) Long userId) {
        log.info("Creating session for agent: {}", agentId);
        AgentSession session = agentApplicationService.createSession(agentId, userId);
        return Result.ok(toVO(session));
    }

    /**
     * 关闭会话
     *
     * @param sessionId 会话ID
     * @return 更新后的会话
     */
    @PostMapping("/{sessionId}/close")
    public Result<AgentSessionVO> close(@PathVariable String sessionId) {
        log.info("Closing session: {}", sessionId);
        AgentSession session = agentApplicationService.closeSession(sessionId);
        return Result.ok(toVO(session));
    }

    /**
     * 将实体转换为VO
     */
    private AgentSessionVO toVO(AgentSession session) {
        return AgentSessionVO.builder()
                .id(session.getId())
                .agentId(session.getAgentId())
                .userId(session.getUserId())
                .sessionId(session.getSessionId())
                .title(session.getTitle())
                .status(session.getStatus())
                .messageCount(session.getMessageCount())
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }
}
