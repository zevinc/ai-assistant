package com.aiassistant.modules.mcp.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * MCP传输类型枚举
 */
@Getter
@AllArgsConstructor
public enum McpTransportTypeEnum {

    /**
     * 标准输入输出
     */
    STDIO("stdio", "标准输入输出"),

    /**
     * Server-Sent Events
     */
    SSE("sse", "Server-Sent Events"),

    /**
     * 可流式HTTP
     */
    STREAMABLE_HTTP("streamable_http", "可流式HTTP");

    private final String code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static McpTransportTypeEnum fromCode(String code) {
        for (McpTransportTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown MCP transport type code: " + code);
    }
}
