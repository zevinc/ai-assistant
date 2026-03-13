package com.aiassistant.modules.mcp.domain.service;

import com.aiassistant.modules.mcp.domain.entity.McpServer;

import java.util.List;
import java.util.Map;

/**
 * MCP客户端接口
 * 用于与MCP服务器进行通信
 */
public interface McpClient {

    /**
     * 连接到MCP服务器
     *
     * @param server MCP服务器配置
     * @return 是否连接成功
     */
    boolean connect(McpServer server);

    /**
     * 列出服务器提供的所有工具
     *
     * @param serverId 服务器ID
     * @return 工具列表
     */
    List<Map<String, Object>> listTools(Long serverId);

    /**
     * 调用工具
     *
     * @param serverId 服务器ID
     * @param toolName 工具名称
     * @param params   调用参数
     * @return 调用结果
     */
    Map<String, Object> callTool(Long serverId, String toolName, Map<String, Object> params);

    /**
     * 断开与MCP服务器的连接
     *
     * @param serverId 服务器ID
     */
    void disconnect(Long serverId);

    /**
     * 检查连接状态
     *
     * @param serverId 服务器ID
     * @return 是否已连接
     */
    boolean isConnected(Long serverId);
}
