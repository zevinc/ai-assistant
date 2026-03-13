package com.aiassistant.modules.mcp.infrastructure.repository;

import com.aiassistant.modules.mcp.domain.entity.McpTool;
import com.aiassistant.modules.mcp.domain.repository.McpToolRepository;
import com.aiassistant.modules.mcp.infrastructure.mapper.McpToolMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MCP工具仓储实现
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class McpToolRepositoryImpl implements McpToolRepository {

    private final McpToolMapper mcpToolMapper;

    @Override
    public Optional<McpTool> findById(Long id) {
        return Optional.ofNullable(mcpToolMapper.selectById(id));
    }

    @Override
    public List<McpTool> findByServerId(Long serverId) {
        LambdaQueryWrapper<McpTool> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McpTool::getServerId, serverId);
        return mcpToolMapper.selectList(wrapper);
    }

    @Override
    public Optional<McpTool> findByName(String name) {
        LambdaQueryWrapper<McpTool> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McpTool::getName, name);
        return Optional.ofNullable(mcpToolMapper.selectOne(wrapper));
    }

    @Override
    public Optional<McpTool> findByServerIdAndName(Long serverId, String name) {
        LambdaQueryWrapper<McpTool> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McpTool::getServerId, serverId)
                .eq(McpTool::getName, name);
        return Optional.ofNullable(mcpToolMapper.selectOne(wrapper));
    }

    @Override
    public List<McpTool> findAll() {
        return mcpToolMapper.selectList(null);
    }

    @Override
    public McpTool save(McpTool tool) {
        mcpToolMapper.insert(tool);
        return tool;
    }

    @Override
    public McpTool update(McpTool tool) {
        mcpToolMapper.updateById(tool);
        return tool;
    }

    @Override
    public void deleteById(Long id) {
        mcpToolMapper.deleteById(id);
    }

    @Override
    public void deleteByServerId(Long serverId) {
        LambdaQueryWrapper<McpTool> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McpTool::getServerId, serverId);
        mcpToolMapper.delete(wrapper);
    }
}
