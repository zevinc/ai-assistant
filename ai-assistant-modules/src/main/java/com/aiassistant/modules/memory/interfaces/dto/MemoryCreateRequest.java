package com.aiassistant.modules.memory.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 记忆创建请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoryCreateRequest {

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * Agent ID
     */
    private Long agentId;

    /**
     * 角色（user/assistant/system）
     */
    private String role;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 记忆类型（short_term/long_term/episodic/semantic）
     */
    private String memoryType;
}
