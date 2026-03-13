package com.aiassistant.modules.memory.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 记忆查询请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoryQueryRequest {

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * Agent ID
     */
    private Long agentId;

    /**
     * 记忆类型
     */
    private String memoryType;

    /**
     * 返回数量限制
     */
    private Integer limit;
}
