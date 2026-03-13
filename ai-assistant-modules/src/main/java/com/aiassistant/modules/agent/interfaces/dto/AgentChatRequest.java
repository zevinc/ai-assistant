package com.aiassistant.modules.agent.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agent对话请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentChatRequest {

    /**
     * Agent ID
     */
    private Long agentId;

    /**
     * 会话ID（可选，不传则创建新会话）
     */
    private String sessionId;

    /**
     * 用户消息
     */
    private String message;
}
