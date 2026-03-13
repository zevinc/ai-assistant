package com.aiassistant.modules.mcp.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MCP服务器创建请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpServerCreateRequest {

    /**
     * 服务器名称
     */
    private String name;

    /**
     * 服务器描述
     */
    private String description;

    /**
     * 传输类型：stdio, sse, streamable_http
     */
    private String transportType;

    /**
     * 端点地址
     */
    private String endpoint;

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;
}
