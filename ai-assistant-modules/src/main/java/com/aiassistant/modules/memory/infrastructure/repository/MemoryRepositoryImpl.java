package com.aiassistant.modules.memory.infrastructure.repository;

import com.aiassistant.modules.memory.domain.entity.Memory;
import com.aiassistant.modules.memory.domain.repository.MemoryRepository;
import com.aiassistant.modules.memory.infrastructure.mapper.MemoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 记忆仓储实现
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class MemoryRepositoryImpl implements MemoryRepository {

    private final MemoryMapper memoryMapper;

    @Override
    public Optional<Memory> findById(Long id) {
        return Optional.ofNullable(memoryMapper.selectById(id));
    }

    @Override
    public List<Memory> findBySessionId(String sessionId) {
        LambdaQueryWrapper<Memory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Memory::getSessionId, sessionId)
                .orderByDesc(Memory::getCreatedAt);
        return memoryMapper.selectList(queryWrapper);
    }

    @Override
    public List<Memory> findByAgentId(Long agentId) {
        LambdaQueryWrapper<Memory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Memory::getAgentId, agentId)
                .orderByDesc(Memory::getCreatedAt);
        return memoryMapper.selectList(queryWrapper);
    }

    @Override
    public List<Memory> findBySessionIdAndType(String sessionId, String memoryType) {
        LambdaQueryWrapper<Memory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Memory::getSessionId, sessionId)
                .eq(Memory::getMemoryType, memoryType)
                .orderByDesc(Memory::getCreatedAt);
        return memoryMapper.selectList(queryWrapper);
    }

    @Override
    public Memory save(Memory memory) {
        if (memory.getId() == null) {
            memoryMapper.insert(memory);
        } else {
            memoryMapper.updateById(memory);
        }
        return memory;
    }

    @Override
    public List<Memory> saveBatch(List<Memory> memories) {
        for (Memory memory : memories) {
            save(memory);
        }
        return memories;
    }

    @Override
    public void deleteBySessionId(String sessionId) {
        LambdaQueryWrapper<Memory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Memory::getSessionId, sessionId);
        memoryMapper.delete(queryWrapper);
    }

    @Override
    public void deleteById(Long id) {
        memoryMapper.deleteById(id);
    }
}
