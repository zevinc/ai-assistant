package com.aiassistant.modules.mcp.domain.repository;

import com.aiassistant.modules.mcp.domain.entity.McpTool;

import java.util.List;
import java.util.Optional;

/**
 * MCP工具仓储接口
 */
public interface McpToolRepository {

    /**
     * 根据ID查询工具
     */
    Optional<McpTool> findById(Long id);

    /**
     * 根据服务器ID查询工具列表
     */
    List<McpTool> findByServerId(Long serverId);

    /**
     * 根据名称查询工具
     */
    Optional<McpTool> findByName(String name);

    /**
     * 根据服务器ID和名称查询工具
     */
    Optional<McpTool> findByServerIdAndName(Long serverId, String name);

    /**
     * 查询所有工具
     */
    List<McpTool> findAll();

    /**
     * 保存工具
     */
    McpTool save(McpTool tool);

    /**
     * 更新工具
     */
    McpTool update(McpTool tool);

    /**
     * 根据ID删除工具
     */
    void deleteById(Long id);

    /**
     * 根据服务器ID删除所有工具
     */
    void deleteByServerId(Long serverId);
}
