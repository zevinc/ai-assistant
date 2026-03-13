package com.aiassistant.modules.mcp.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * MCP服务器视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpServerVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 服务器名称
     */
    private String name;

    /**
     * 服务器描述
     */
    private String description;

    /**
     * 传输类型
     */
    private String transportType;

    /**
     * 端点地址
     */
    private String endpoint;

    /**
     * API密钥（脱敏显示）
     */
    private String apiKey;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 连接状态
     */
    private Boolean connected;

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
    public static McpServerVO fromEntity(com.aiassistant.modules.mcp.domain.entity.McpServer server) {
        return McpServerVO.builder()
                .id(server.getId())
                .name(server.getName())
                .description(server.getDescription())
                .transportType(server.getTransportType())
                .endpoint(server.getEndpoint())
                .apiKey(maskApiKey(server.getApiKey()))
                .status(server.getStatus())
                .createdAt(server.getCreatedAt())
                .updatedAt(server.getUpdatedAt())
                .createdBy(server.getCreatedBy())
                .updatedBy(server.getUpdatedBy())
                .build();
    }

    /**
     * 从实体转换为VO（包含连接状态）
     */
    public static McpServerVO fromEntity(com.aiassistant.modules.mcp.domain.entity.McpServer server, boolean connected) {
        return McpServerVO.builder()
                .id(server.getId())
                .name(server.getName())
                .description(server.getDescription())
                .transportType(server.getTransportType())
                .endpoint(server.getEndpoint())
                .apiKey(maskApiKey(server.getApiKey()))
                .status(server.getStatus())
                .connected(connected)
                .createdAt(server.getCreatedAt())
                .updatedAt(server.getUpdatedAt())
                .createdBy(server.getCreatedBy())
                .updatedBy(server.getUpdatedBy())
                .build();
    }

    /**
     * 脱敏API密钥
     */
    private static String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.length() <= 8) {
            return apiKey;
        }
        return apiKey.substring(0, 4) + "****" + apiKey.substring(apiKey.length() - 4);
    }
}
