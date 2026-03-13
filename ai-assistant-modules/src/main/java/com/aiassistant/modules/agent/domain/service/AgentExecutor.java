package com.aiassistant.modules.agent.domain.service;

import com.aiassistant.core.orchestration.model.OrchestrationRequest;
import com.aiassistant.core.orchestration.model.OrchestrationResponse;
import com.aiassistant.core.orchestration.service.OrchestrationEngine;
import com.aiassistant.modules.agent.domain.entity.Agent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Agent执行器
 * Agent执行的主入口点，负责协调编排引擎执行用户请求
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AgentExecutor {

    private final OrchestrationEngine orchestrationEngine;

    /**
     * 执行Agent处理用户消息
     *
     * @param agent       Agent实体
     * @param sessionId   会话ID
     * @param userMessage 用户消息
     * @return 响应内容
     */
    public String execute(Agent agent, String sessionId, String userMessage) {
        log.info("Executing agent: {} for session: {}", agent.getName(), sessionId);

        // 构建编排请求
        OrchestrationRequest request = OrchestrationRequest.builder()
                .agentId(agent.getId())
                .sessionId(sessionId)
                .userMessage(userMessage)
                .stream(false)
                .build();

        // 调用编排引擎执行
        OrchestrationResponse response = orchestrationEngine.execute(request);

        log.info("Agent execution completed for session: {}, tokens used: {}",
                sessionId, response.getTokensUsed());

        return response.getContent();
    }

    /**
     * 执行Agent处理用户消息（带流式输出）
     *
     * @param agent       Agent实体
     * @param sessionId   会话ID
     * @param userMessage 用户消息
     * @return 响应内容
     */
    public String executeStreaming(Agent agent, String sessionId, String userMessage) {
        log.info("Executing agent with streaming: {} for session: {}", agent.getName(), sessionId);

        // 构建流式编排请求
        OrchestrationRequest request = OrchestrationRequest.streaming(
                agent.getId(),
                sessionId,
                userMessage
        );

        // 调用编排引擎执行
        OrchestrationResponse response = orchestrationEngine.execute(request);

        return response.getContent();
    }
}
