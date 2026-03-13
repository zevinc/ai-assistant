package com.aiassistant.modules.agent.application.service;

import com.aiassistant.common.event.DomainEventPublisher;
import com.aiassistant.common.event.SimpleDomainEvent;
import com.aiassistant.modules.agent.domain.entity.Agent;
import com.aiassistant.modules.agent.domain.entity.AgentSession;
import com.aiassistant.modules.agent.domain.enums.AgentStatusEnum;
import com.aiassistant.modules.agent.domain.enums.SessionStatusEnum;
import com.aiassistant.modules.agent.domain.repository.AgentRepository;
import com.aiassistant.modules.agent.domain.repository.AgentSessionRepository;
import com.aiassistant.modules.agent.domain.service.AgentExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Agent应用服务
 * 提供Agent管理的业务用例
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AgentApplicationService {

    private final AgentRepository agentRepository;
    private final AgentSessionRepository agentSessionRepository;
    private final AgentExecutor agentExecutor;
    private final DomainEventPublisher eventPublisher;

    /**
     * 创建Agent
     *
     * @param name          Agent名称
     * @param description   Agent描述
     * @param specId        规格ID
     * @param modelId       模型ID
     * @param systemPrompt  系统提示词
     * @param temperature   温度参数
     * @param maxTokens     最大Token数
     * @param enableMemory  是否启用记忆
     * @param enablePlanning 是否启用规划
     * @param enableTools   是否启用工具
     * @return 创建的Agent
     */
    @Transactional
    public Agent createAgent(String name, String description, Long specId, String modelId,
                             String systemPrompt, Double temperature, Integer maxTokens,
                             Boolean enableMemory, Boolean enablePlanning, Boolean enableTools) {
        log.info("Creating agent: {}", name);

        Agent agent = Agent.builder()
                .name(name)
                .description(description)
                .specId(specId)
                .modelId(modelId)
                .systemPrompt(systemPrompt)
                .temperature(temperature != null ? temperature : 0.7)
                .maxTokens(maxTokens != null ? maxTokens : 4096)
                .enableMemory(enableMemory != null ? enableMemory : true)
                .enablePlanning(enablePlanning != null ? enablePlanning : false)
                .enableTools(enableTools != null ? enableTools : true)
                .status(AgentStatusEnum.DRAFT.getCode())
                .build();

        Agent savedAgent = agentRepository.save(agent);

        // 发布Agent创建事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(savedAgent.getId()), "Agent", "AgentCreated"));

        log.info("Agent created successfully with id: {}", savedAgent.getId());
        return savedAgent;
    }

    /**
     * 更新Agent
     *
     * @param id            Agent ID
     * @param name          Agent名称
     * @param description   Agent描述
     * @param systemPrompt  系统提示词
     * @param temperature   温度参数
     * @param maxTokens     最大Token数
     * @param enableMemory  是否启用记忆
     * @param enablePlanning 是否启用规划
     * @param enableTools   是否启用工具
     * @return 更新后的Agent
     */
    @Transactional
    public Agent updateAgent(Long id, String name, String description, String systemPrompt,
                             Double temperature, Integer maxTokens, Boolean enableMemory,
                             Boolean enablePlanning, Boolean enableTools) {
        log.info("Updating agent: {}", id);

        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found: " + id));

        if (name != null) {
            agent.setName(name);
        }
        if (description != null) {
            agent.setDescription(description);
        }
        if (systemPrompt != null) {
            agent.setSystemPrompt(systemPrompt);
        }
        if (temperature != null) {
            agent.setTemperature(temperature);
        }
        if (maxTokens != null) {
            agent.setMaxTokens(maxTokens);
        }
        if (enableMemory != null) {
            agent.setEnableMemory(enableMemory);
        }
        if (enablePlanning != null) {
            agent.setEnablePlanning(enablePlanning);
        }
        if (enableTools != null) {
            agent.setEnableTools(enableTools);
        }

        Agent updatedAgent = agentRepository.update(agent);

        // 发布Agent更新事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(id), "Agent", "AgentUpdated"));

        log.info("Agent updated successfully: {}", id);
        return updatedAgent;
    }

    /**
     * 删除Agent
     *
     * @param id Agent ID
     */
    @Transactional
    public void deleteAgent(Long id) {
        log.info("Deleting agent: {}", id);

        agentRepository.deleteById(id);

        // 发布Agent删除事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(id), "Agent", "AgentDeleted"));

        log.info("Agent deleted successfully: {}", id);
    }

    /**
     * 激活Agent
     *
     * @param id Agent ID
     * @return 更新后的Agent
     */
    @Transactional
    public Agent activateAgent(Long id) {
        log.info("Activating agent: {}", id);

        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found: " + id));

        agent.setStatus(AgentStatusEnum.ACTIVE.getCode());
        Agent updatedAgent = agentRepository.update(agent);

        log.info("Agent activated successfully: {}", id);
        return updatedAgent;
    }

    /**
     * 与Agent对话
     *
     * @param agentId   Agent ID
     * @param sessionId 会话ID
     * @param message   用户消息
     * @return 响应内容
     */
    @Transactional
    public String chat(Long agentId, String sessionId, String message) {
        log.info("Chatting with agent: {}, session: {}", agentId, sessionId);

        // 获取Agent
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found: " + agentId));

        // 检查Agent状态
        if (agent.getStatus() != AgentStatusEnum.ACTIVE.getCode()) {
            throw new IllegalStateException("Agent is not active: " + agentId);
        }

        // 获取或创建会话
        AgentSession session;
        final String effectiveSessionId;
        if (sessionId == null || sessionId.isEmpty()) {
            session = createSession(agentId, null);
            effectiveSessionId = session.getSessionId();
        } else {
            effectiveSessionId = sessionId;
            session = agentSessionRepository.findBySessionId(sessionId)
                    .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));
        }

        // 执行Agent
        String response = agentExecutor.execute(agent, effectiveSessionId, message);

        // 更新会话消息数量
        session.setMessageCount(session.getMessageCount() != null ? session.getMessageCount() + 2 : 2);
        agentSessionRepository.update(session);

        // 发布对话事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(agentId), "Agent", "AgentChat"));

        log.info("Chat completed for agent: {}, session: {}", agentId, effectiveSessionId);
        return response;
    }

    /**
     * 列出Agent的所有会话
     *
     * @param agentId Agent ID
     * @return 会话列表
     */
    public List<AgentSession> listSessions(Long agentId) {
        return agentSessionRepository.findByAgentId(agentId);
    }

    /**
     * 创建新会话
     *
     * @param agentId Agent ID
     * @param userId  用户ID
     * @return 创建的会话
     */
    @Transactional
    public AgentSession createSession(Long agentId, Long userId) {
        log.info("Creating session for agent: {}", agentId);

        String sessionId = UUID.randomUUID().toString();

        AgentSession session = AgentSession.builder()
                .agentId(agentId)
                .userId(userId)
                .sessionId(sessionId)
                .status(SessionStatusEnum.ACTIVE.getCode())
                .messageCount(0)
                .build();

        AgentSession savedSession = agentSessionRepository.save(session);

        log.info("Session created successfully: {}", sessionId);
        return savedSession;
    }

    /**
     * 关闭会话
     *
     * @param sessionId 会话ID
     * @return 更新后的会话
     */
    @Transactional
    public AgentSession closeSession(String sessionId) {
        log.info("Closing session: {}", sessionId);

        AgentSession session = agentSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        session.setStatus(SessionStatusEnum.CLOSED.getCode());
        AgentSession updatedSession = agentSessionRepository.update(session);

        log.info("Session closed successfully: {}", sessionId);
        return updatedSession;
    }
}
