package com.aiassistant.modules.mcp.domain.repository;

import com.aiassistant.modules.mcp.domain.entity.McpServer;

import java.util.List;
import java.util.Optional;

/**
 * MCP服务器仓储接口
 */
public interface McpServerRepository {

    /**
     * 根据ID查询服务器
     */
    Optional<McpServer> findById(Long id);

    /**
     * 根据名称查询服务器
     */
    Optional<McpServer> findByName(String name);

    /**
     * 查询所有服务器
     */
    List<McpServer> listAll();

    /**
     * 查询所有启用的服务器
     */
    List<McpServer> listAllEnabled();

    /**
     * 保存服务器
     */
    McpServer save(McpServer server);

    /**
     * 更新服务器
     */
    McpServer update(McpServer server);

    /**
     * 根据ID删除服务器
     */
    void deleteById(Long id);
}
