package com.aiassistant.modules.mcp.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * MCP服务器实体
 * 定义Model Context Protocol服务器配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_mcp_server")
public class McpServer extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
