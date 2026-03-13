package com.aiassistant.modules.agent.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.agent.application.service.AgentApplicationService;
import com.aiassistant.modules.agent.domain.entity.Agent;
import com.aiassistant.modules.agent.interfaces.dto.AgentChatRequest;
import com.aiassistant.modules.agent.interfaces.dto.AgentCreateRequest;
import com.aiassistant.modules.agent.interfaces.vo.AgentVO;
import com.aiassistant.modules.agent.interfaces.vo.ChatResponseVO;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Agent控制器
 * 提供Agent管理的REST API
 */
@RestController
@RequestMapping("/api/v1/agents")
@Slf4j
@RequiredArgsConstructor
public class AgentController {

    private final AgentApplicationService agentApplicationService;

    /**
     * 创建Agent
     *
     * @param request Agent创建请求
     * @return 创建的Agent
     */
    @PostMapping
    public Result<AgentVO> create(@RequestBody AgentCreateRequest request) {
        log.info("Creating agent: {}", request.getName());

        Agent agent = agentApplicationService.createAgent(
                request.getName(),
                request.getDescription(),
                request.getSpecId(),
                request.getModelId(),
                request.getSystemPrompt(),
                request.getTemperature(),
                request.getMaxTokens(),
                request.getEnableMemory(),
                request.getEnablePlanning(),
                request.getEnableTools()
        );

        return Result.ok(toVO(agent));
    }

    /**
     * 获取Agent详情
     *
     * @param id Agent ID
     * @return Agent详情
     */
    @GetMapping("/{id}")
    public Result<AgentVO> get(@PathVariable Long id) {
        log.info("Getting agent: {}", id);
        // TODO: 实现获取单个Agent的方法
        return Result.ok();
    }

    /**
     * 更新Agent
     *
     * @param id      Agent ID
     * @param request Agent更新请求
     * @return 更新后的Agent
     */
    @PutMapping("/{id}")
    public Result<AgentVO> update(@PathVariable Long id, @RequestBody AgentCreateRequest request) {
        log.info("Updating agent: {}", id);

        Agent agent = agentApplicationService.updateAgent(
                id,
                request.getName(),
                request.getDescription(),
                request.getSystemPrompt(),
                request.getTemperature(),
                request.getMaxTokens(),
                request.getEnableMemory(),
                request.getEnablePlanning(),
                request.getEnableTools()
        );

        return Result.ok(toVO(agent));
    }

    /**
     * 删除Agent
     *
     * @param id Agent ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("Deleting agent: {}", id);
        agentApplicationService.deleteAgent(id);
        return Result.ok();
    }

    /**
     * 激活Agent
     *
     * @param id Agent ID
     * @return 更新后的Agent
     */
    @PostMapping("/{id}/activate")
    public Result<AgentVO> activate(@PathVariable Long id) {
        log.info("Activating agent: {}", id);
        Agent agent = agentApplicationService.activateAgent(id);
        return Result.ok(toVO(agent));
    }

    /**
     * 与Agent对话
     *
     * @param request 对话请求
     * @return 对话响应
     */
    @PostMapping("/chat")
    public Result<ChatResponseVO> chat(@RequestBody AgentChatRequest request) {
        log.info("Chatting with agent: {}", request.getAgentId());

        String response = agentApplicationService.chat(
                request.getAgentId(),
                request.getSessionId(),
                request.getMessage()
        );

        ChatResponseVO chatResponse = ChatResponseVO.builder()
                .sessionId(request.getSessionId())
                .response(response)
                .timestamp(LocalDateTime.now())
                .build();

        return Result.ok(chatResponse);
    }

    /**
     * 将实体转换为VO
     */
    private AgentVO toVO(Agent agent) {
        return AgentVO.builder()
                .id(agent.getId())
                .name(agent.getName())
                .description(agent.getDescription())
                .specId(agent.getSpecId())
                .modelId(agent.getModelId())
                .systemPrompt(agent.getSystemPrompt())
                .temperature(agent.getTemperature())
                .maxTokens(agent.getMaxTokens())
                .enableMemory(agent.getEnableMemory())
                .enablePlanning(agent.getEnablePlanning())
                .enableTools(agent.getEnableTools())
                .status(agent.getStatus())
                .createdAt(agent.getCreatedAt())
                .updatedAt(agent.getUpdatedAt())
                .build();
    }
}
