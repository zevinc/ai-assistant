package com.aiassistant.modules.mcp.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * MCP工具视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpToolVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 所属服务器ID
     */
    private Long serverId;

    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 工具名称
     */
    private String name;

    /**
     * 工具描述
     */
    private String description;

    /**
     * 输入参数Schema
     */
    private String inputSchema;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 从实体转换为VO
     */
    public static McpToolVO fromEntity(com.aiassistant.modules.mcp.domain.entity.McpTool tool) {
        return McpToolVO.builder()
                .id(tool.getId())
                .serverId(tool.getServerId())
                .name(tool.getName())
                .description(tool.getDescription())
                .inputSchema(tool.getInputSchema())
                .status(tool.getStatus())
                .createdAt(tool.getCreatedAt())
                .updatedAt(tool.getUpdatedAt())
                .createdBy(tool.getCreatedBy())
                .updatedBy(tool.getUpdatedBy())
                .build();
    }

    /**
     * 从实体转换为VO（包含服务器名称）
     */
    public static McpToolVO fromEntity(com.aiassistant.modules.mcp.domain.entity.McpTool tool, String serverName) {
        return McpToolVO.builder()
                .id(tool.getId())
                .serverId(tool.getServerId())
                .serverName(serverName)
                .name(tool.getName())
                .description(tool.getDescription())
                .inputSchema(tool.getInputSchema())
                .status(tool.getStatus())
                .createdAt(tool.getCreatedAt())
                .updatedAt(tool.getUpdatedAt())
                .createdBy(tool.getCreatedBy())
                .updatedBy(tool.getUpdatedBy())
                .build();
    }
}
