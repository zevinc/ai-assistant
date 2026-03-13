package com.aiassistant.modules.mcp.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * MCP工具调用请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpToolCallRequest {

    /**
     * 服务器ID
     */
    private Long serverId;

    /**
     * 工具名称
     */
    private String toolName;

    /**
     * 调用参数
     */
    private Map<String, Object> params;
}
