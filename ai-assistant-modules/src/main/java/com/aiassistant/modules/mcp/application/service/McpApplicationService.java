package com.aiassistant.modules.mcp.application.service;

import com.aiassistant.common.event.DomainEventPublisher;
import com.aiassistant.common.event.SimpleDomainEvent;
import com.aiassistant.modules.mcp.domain.entity.McpServer;
import com.aiassistant.modules.mcp.domain.entity.McpTool;
import com.aiassistant.modules.mcp.domain.repository.McpServerRepository;
import com.aiassistant.modules.mcp.domain.repository.McpToolRepository;
import com.aiassistant.modules.mcp.domain.service.McpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * MCP应用服务
 * 提供MCP服务器和工具的CRUD功能
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class McpApplicationService {

    private final McpServerRepository mcpServerRepository;
    private final McpToolRepository mcpToolRepository;
    private final McpClient mcpClient;
    private final DomainEventPublisher domainEventPublisher;

    // ==================== 服务器管理 ====================

    /**
     * 创建MCP服务器
     */
    @Transactional
    public McpServer createServer(McpServer server) {
        log.info("Creating MCP server: name={}, transportType={}", server.getName(), server.getTransportType());

        // 检查名称是否已存在
        Optional<McpServer> existingServer = mcpServerRepository.findByName(server.getName());
        if (existingServer.isPresent()) {
            throw new IllegalArgumentException("MCP server with name '" + server.getName() + "' already exists");
        }

        McpServer savedServer = mcpServerRepository.save(server);

        // 发布领域事件
        publishServerEvent("mcp.server.created", savedServer);

        return savedServer;
    }

    /**
     * 根据ID查询MCP服务器
     */
    public Optional<McpServer> getServerById(Long id) {
        return mcpServerRepository.findById(id);
    }

    /**
     * 根据名称查询MCP服务器
     */
    public Optional<McpServer> getServerByName(String name) {
        return mcpServerRepository.findByName(name);
    }

    /**
     * 查询所有MCP服务器
     */
    public List<McpServer> getAllServers() {
        return mcpServerRepository.listAll();
    }

    /**
     * 查询所有启用的MCP服务器
     */
    public List<McpServer> getAllEnabledServers() {
        return mcpServerRepository.listAllEnabled();
    }

    /**
     * 更新MCP服务器
     */
    @Transactional
    public McpServer updateServer(Long id, McpServer server) {
        log.info("Updating MCP server: id={}", id);

        McpServer existingServer = mcpServerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MCP server not found with id: " + id));

        // 更新字段
        if (server.getName() != null) {
            existingServer.setName(server.getName());
        }
        if (server.getDescription() != null) {
            existingServer.setDescription(server.getDescription());
        }
        if (server.getTransportType() != null) {
            existingServer.setTransportType(server.getTransportType());
        }
        if (server.getEndpoint() != null) {
            existingServer.setEndpoint(server.getEndpoint());
        }
        if (server.getApiKey() != null) {
            existingServer.setApiKey(server.getApiKey());
        }
        if (server.getStatus() != null) {
            existingServer.setStatus(server.getStatus());
        }

        McpServer updatedServer = mcpServerRepository.update(existingServer);

        // 发布领域事件
        publishServerEvent("mcp.server.updated", updatedServer);

        return updatedServer;
    }

    /**
     * 删除MCP服务器
     */
    @Transactional
    public void deleteServer(Long id) {
        log.info("Deleting MCP server: id={}", id);

        McpServer server = mcpServerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MCP server not found with id: " + id));

        // 断开连接
        if (mcpClient.isConnected(id)) {
            mcpClient.disconnect(id);
        }

        // 删除关联的工具
        mcpToolRepository.deleteByServerId(id);

        // 删除服务器
        mcpServerRepository.deleteById(id);

        // 发布领域事件
        publishServerEvent("mcp.server.deleted", server);
    }

    /**
     * 连接到MCP服务器
     */
    public boolean connectServer(Long serverId) {
        log.info("Connecting to MCP server: serverId={}", serverId);

        McpServer server = mcpServerRepository.findById(serverId)
                .orElseThrow(() -> new IllegalArgumentException("MCP server not found with id: " + serverId));

        if (server.getStatus() == null || server.getStatus() != 1) {
            throw new IllegalStateException("MCP server is not enabled: " + serverId);
        }

        boolean connected = mcpClient.connect(server);

        if (connected) {
            // 连接成功后，同步工具列表
            syncToolsFromServer(serverId);
        }

        return connected;
    }

    /**
     * 断开MCP服务器连接
     */
    public void disconnectServer(Long serverId) {
        log.info("Disconnecting from MCP server: serverId={}", serverId);
        mcpClient.disconnect(serverId);
    }

    /**
     * 检查服务器连接状态
     */
    public boolean isServerConnected(Long serverId) {
        return mcpClient.isConnected(serverId);
    }

    // ==================== 工具管理 ====================

    /**
     * 根据ID查询MCP工具
     */
    public Optional<McpTool> getToolById(Long id) {
        return mcpToolRepository.findById(id);
    }

    /**
     * 根据服务器ID查询工具列表
     */
    public List<McpTool> getToolsByServerId(Long serverId) {
        return mcpToolRepository.findByServerId(serverId);
    }

    /**
     * 查询所有MCP工具
     */
    public List<McpTool> getAllTools() {
        return mcpToolRepository.findAll();
    }

    /**
     * 创建MCP工具
     */
    @Transactional
    public McpTool createTool(McpTool tool) {
        log.info("Creating MCP tool: name={}, serverId={}", tool.getName(), tool.getServerId());

        // 检查服务器是否存在
        mcpServerRepository.findById(tool.getServerId())
                .orElseThrow(() -> new IllegalArgumentException("MCP server not found with id: " + tool.getServerId()));

        McpTool savedTool = mcpToolRepository.save(tool);

        // 发布领域事件
        publishToolEvent("mcp.tool.created", savedTool);

        return savedTool;
    }

    /**
     * 更新MCP工具
     */
    @Transactional
    public McpTool updateTool(Long id, McpTool tool) {
        log.info("Updating MCP tool: id={}", id);

        McpTool existingTool = mcpToolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MCP tool not found with id: " + id));

        // 更新字段
        if (tool.getName() != null) {
            existingTool.setName(tool.getName());
        }
        if (tool.getDescription() != null) {
            existingTool.setDescription(tool.getDescription());
        }
        if (tool.getInputSchema() != null) {
            existingTool.setInputSchema(tool.getInputSchema());
        }
        if (tool.getStatus() != null) {
            existingTool.setStatus(tool.getStatus());
        }

        McpTool updatedTool = mcpToolRepository.update(existingTool);

        // 发布领域事件
        publishToolEvent("mcp.tool.updated", updatedTool);

        return updatedTool;
    }

    /**
     * 删除MCP工具
     */
    @Transactional
    public void deleteTool(Long id) {
        log.info("Deleting MCP tool: id={}", id);

        McpTool tool = mcpToolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MCP tool not found with id: " + id));

        mcpToolRepository.deleteById(id);

        // 发布领域事件
        publishToolEvent("mcp.tool.deleted", tool);
    }

    /**
     * 从服务器同步工具列表
     */
    @Transactional
    public void syncToolsFromServer(Long serverId) {
        log.info("Syncing tools from server: serverId={}", serverId);

        if (!mcpClient.isConnected(serverId)) {
            throw new IllegalStateException("Server not connected: " + serverId);
        }

        List<Map<String, Object>> tools = mcpClient.listTools(serverId);

        // 删除旧的工具
        mcpToolRepository.deleteByServerId(serverId);

        // 保存新的工具
        for (Map<String, Object> toolInfo : tools) {
            McpTool tool = McpTool.builder()
                    .serverId(serverId)
                    .name((String) toolInfo.get("name"))
                    .description((String) toolInfo.get("description"))
                    .inputSchema((String) toolInfo.get("inputSchema"))
                    .status(1)
                    .build();
            mcpToolRepository.save(tool);
        }

        log.info("Synced {} tools from server: serverId={}", tools.size(), serverId);
    }

    /**
     * 调用MCP工具
     */
    public Map<String, Object> callTool(Long serverId, String toolName, Map<String, Object> params) {
        log.info("Calling MCP tool: serverId={}, toolName={}", serverId, toolName);

        // 确保服务器已连接
        if (!mcpClient.isConnected(serverId)) {
            McpServer server = mcpServerRepository.findById(serverId)
                    .orElseThrow(() -> new IllegalArgumentException("MCP server not found with id: " + serverId));

            boolean connected = mcpClient.connect(server);
            if (!connected) {
                throw new IllegalStateException("Failed to connect to MCP server: " + serverId);
            }
        }

        return mcpClient.callTool(serverId, toolName, params);
    }

    // ==================== 事件发布 ====================

    /**
     * 发布服务器领域事件
     */
    private void publishServerEvent(String eventType, McpServer server) {
        try {
            domainEventPublisher.publish(new SimpleDomainEvent(String.valueOf(server.getId()), "McpServer", eventType));
        } catch (Exception e) {
            log.warn("Failed to publish domain event: {}", e.getMessage());
        }
    }

    /**
     * 发布工具领域事件
     */
    private void publishToolEvent(String eventType, McpTool tool) {
        try {
            domainEventPublisher.publish(new SimpleDomainEvent(String.valueOf(tool.getId()), "McpTool", eventType));
        } catch (Exception e) {
            log.warn("Failed to publish domain event: {}", e.getMessage());
        }
    }
}
