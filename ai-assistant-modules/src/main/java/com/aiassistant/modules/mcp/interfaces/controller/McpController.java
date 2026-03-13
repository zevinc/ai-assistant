package com.aiassistant.modules.mcp.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.mcp.application.service.McpApplicationService;
import com.aiassistant.modules.mcp.domain.entity.McpServer;
import com.aiassistant.modules.mcp.domain.entity.McpTool;
import com.aiassistant.modules.mcp.interfaces.dto.McpServerCreateRequest;
import com.aiassistant.modules.mcp.interfaces.dto.McpToolCallRequest;
import com.aiassistant.modules.mcp.interfaces.vo.McpServerVO;
import com.aiassistant.modules.mcp.interfaces.vo.McpToolVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MCP控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/mcp")
@RequiredArgsConstructor
public class McpController {

    private final McpApplicationService mcpApplicationService;

    // ==================== 服务器管理 ====================

    /**
     * 创建MCP服务器
     */
    @PostMapping("/servers")
    public Result<McpServerVO> createServer(@RequestBody McpServerCreateRequest request) {
        log.info("Creating MCP server: name={}", request.getName());

        McpServer server = McpServer.builder()
                .name(request.getName())
                .description(request.getDescription())
                .transportType(request.getTransportType())
                .endpoint(request.getEndpoint())
                .apiKey(request.getApiKey())
                .status(request.getStatus() != null ? request.getStatus() : 1)
                .build();

        McpServer createdServer = mcpApplicationService.createServer(server);
        return Result.ok(McpServerVO.fromEntity(createdServer));
    }

    /**
     * 根据ID查询MCP服务器
     */
    @GetMapping("/servers/{id}")
    public Result<McpServerVO> getServerById(@PathVariable Long id) {
        log.info("Getting MCP server by id: {}", id);
        return mcpApplicationService.getServerById(id)
                .map(server -> {
                    boolean connected = mcpApplicationService.isServerConnected(id);
                    return Result.ok(McpServerVO.fromEntity(server, connected));
                })
                .orElse(Result.fail(404, "MCP server not found"));
    }

    /**
     * 根据名称查询MCP服务器
     */
    @GetMapping("/servers/name/{name}")
    public Result<McpServerVO> getServerByName(@PathVariable String name) {
        log.info("Getting MCP server by name: {}", name);
        return mcpApplicationService.getServerByName(name)
                .map(server -> {
                    boolean connected = mcpApplicationService.isServerConnected(server.getId());
                    return Result.ok(McpServerVO.fromEntity(server, connected));
                })
                .orElse(Result.fail(404, "MCP server not found"));
    }

    /**
     * 查询所有MCP服务器
     */
    @GetMapping("/servers")
    public Result<List<McpServerVO>> getAllServers() {
        log.info("Getting all MCP servers");
        List<McpServerVO> servers = mcpApplicationService.getAllServers().stream()
                .map(server -> {
                    boolean connected = mcpApplicationService.isServerConnected(server.getId());
                    return McpServerVO.fromEntity(server, connected);
                })
                .collect(Collectors.toList());
        return Result.ok(servers);
    }

    /**
     * 查询所有启用的MCP服务器
     */
    @GetMapping("/servers/enabled")
    public Result<List<McpServerVO>> getAllEnabledServers() {
        log.info("Getting all enabled MCP servers");
        List<McpServerVO> servers = mcpApplicationService.getAllEnabledServers().stream()
                .map(server -> {
                    boolean connected = mcpApplicationService.isServerConnected(server.getId());
                    return McpServerVO.fromEntity(server, connected);
                })
                .collect(Collectors.toList());
        return Result.ok(servers);
    }

    /**
     * 更新MCP服务器
     */
    @PutMapping("/servers/{id}")
    public Result<McpServerVO> updateServer(@PathVariable Long id, @RequestBody McpServerCreateRequest request) {
        log.info("Updating MCP server: id={}", id);

        McpServer server = McpServer.builder()
                .name(request.getName())
                .description(request.getDescription())
                .transportType(request.getTransportType())
                .endpoint(request.getEndpoint())
                .apiKey(request.getApiKey())
                .status(request.getStatus())
                .build();

        McpServer updatedServer = mcpApplicationService.updateServer(id, server);
        return Result.ok(McpServerVO.fromEntity(updatedServer));
    }

    /**
     * 删除MCP服务器
     */
    @DeleteMapping("/servers/{id}")
    public Result<Void> deleteServer(@PathVariable Long id) {
        log.info("Deleting MCP server: id={}", id);
        mcpApplicationService.deleteServer(id);
        return Result.ok();
    }

    /**
     * 连接到MCP服务器
     */
    @PostMapping("/servers/{id}/connect")
    public Result<Boolean> connectServer(@PathVariable Long id) {
        log.info("Connecting to MCP server: id={}", id);
        boolean connected = mcpApplicationService.connectServer(id);
        return Result.ok(connected);
    }

    /**
     * 断开MCP服务器连接
     */
    @PostMapping("/servers/{id}/disconnect")
    public Result<Void> disconnectServer(@PathVariable Long id) {
        log.info("Disconnecting from MCP server: id={}", id);
        mcpApplicationService.disconnectServer(id);
        return Result.ok();
    }

    /**
     * 检查服务器连接状态
     */
    @GetMapping("/servers/{id}/status")
    public Result<Boolean> getServerConnectionStatus(@PathVariable Long id) {
        log.info("Getting connection status for MCP server: id={}", id);
        boolean connected = mcpApplicationService.isServerConnected(id);
        return Result.ok(connected);
    }

    /**
     * 同步服务器工具列表
     */
    @PostMapping("/servers/{id}/sync-tools")
    public Result<Void> syncToolsFromServer(@PathVariable Long id) {
        log.info("Syncing tools from MCP server: id={}", id);
        mcpApplicationService.syncToolsFromServer(id);
        return Result.ok();
    }

    // ==================== 工具管理 ====================

    /**
     * 根据ID查询MCP工具
     */
    @GetMapping("/tools/{id}")
    public Result<McpToolVO> getToolById(@PathVariable Long id) {
        log.info("Getting MCP tool by id: {}", id);
        return mcpApplicationService.getToolById(id)
                .map(tool -> {
                    String serverName = mcpApplicationService.getServerById(tool.getServerId())
                            .map(McpServer::getName)
                            .orElse(null);
                    return Result.ok(McpToolVO.fromEntity(tool, serverName));
                })
                .orElse(Result.fail(404, "MCP tool not found"));
    }

    /**
     * 根据服务器ID查询工具列表
     */
    @GetMapping("/servers/{serverId}/tools")
    public Result<List<McpToolVO>> getToolsByServerId(@PathVariable Long serverId) {
        log.info("Getting MCP tools by serverId: {}", serverId);
        String serverName = mcpApplicationService.getServerById(serverId)
                .map(McpServer::getName)
                .orElse(null);
        List<McpToolVO> tools = mcpApplicationService.getToolsByServerId(serverId).stream()
                .map(tool -> McpToolVO.fromEntity(tool, serverName))
                .collect(Collectors.toList());
        return Result.ok(tools);
    }

    /**
     * 查询所有MCP工具
     */
    @GetMapping("/tools")
    public Result<List<McpToolVO>> getAllTools() {
        log.info("Getting all MCP tools");
        List<McpToolVO> tools = mcpApplicationService.getAllTools().stream()
                .map(tool -> {
                    String serverName = mcpApplicationService.getServerById(tool.getServerId())
                            .map(McpServer::getName)
                            .orElse(null);
                    return McpToolVO.fromEntity(tool, serverName);
                })
                .collect(Collectors.toList());
        return Result.ok(tools);
    }

    /**
     * 调用MCP工具
     */
    @PostMapping("/tools/call")
    public Result<Map<String, Object>> callTool(@RequestBody McpToolCallRequest request) {
        log.info("Calling MCP tool: serverId={}, toolName={}", request.getServerId(), request.getToolName());
        Map<String, Object> result = mcpApplicationService.callTool(
                request.getServerId(),
                request.getToolName(),
                request.getParams()
        );
        return Result.ok(result);
    }
}
