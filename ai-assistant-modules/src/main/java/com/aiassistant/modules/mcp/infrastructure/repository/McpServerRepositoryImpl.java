package com.aiassistant.modules.mcp.infrastructure.repository;

import com.aiassistant.modules.mcp.domain.entity.McpServer;
import com.aiassistant.modules.mcp.domain.repository.McpServerRepository;
import com.aiassistant.modules.mcp.infrastructure.mapper.McpServerMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MCP服务器仓储实现
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class McpServerRepositoryImpl implements McpServerRepository {

    private final McpServerMapper mcpServerMapper;

    @Override
    public Optional<McpServer> findById(Long id) {
        return Optional.ofNullable(mcpServerMapper.selectById(id));
    }

    @Override
    public Optional<McpServer> findByName(String name) {
        LambdaQueryWrapper<McpServer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McpServer::getName, name);
        return Optional.ofNullable(mcpServerMapper.selectOne(wrapper));
    }

    @Override
    public List<McpServer> listAll() {
        return mcpServerMapper.selectList(null);
    }

    @Override
    public List<McpServer> listAllEnabled() {
        LambdaQueryWrapper<McpServer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(McpServer::getStatus, 1);
        return mcpServerMapper.selectList(wrapper);
    }

    @Override
    public McpServer save(McpServer server) {
        mcpServerMapper.insert(server);
        return server;
    }

    @Override
    public McpServer update(McpServer server) {
        mcpServerMapper.updateById(server);
        return server;
    }

    @Override
    public void deleteById(Long id) {
        mcpServerMapper.deleteById(id);
    }
}
