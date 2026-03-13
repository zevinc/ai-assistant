package com.aiassistant.modules.mcp.infrastructure.mapper;

import com.aiassistant.modules.mcp.domain.entity.McpTool;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * MCP工具Mapper接口
 */
@Mapper
public interface McpToolMapper extends BaseMapper<McpTool> {
}
