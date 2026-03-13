package com.aiassistant.modules.mcp.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * MCP工具实体
 * 定义MCP服务器提供的工具
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_mcp_tool")
public class McpTool extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 所属服务器ID
     */
    private Long serverId;

    /**
     * 工具名称
     */
    private String name;

    /**
     * 工具描述
     */
    private String description;

    /**
     * 输入参数Schema（JSON格式）
     */
    private String inputSchema;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;
}
