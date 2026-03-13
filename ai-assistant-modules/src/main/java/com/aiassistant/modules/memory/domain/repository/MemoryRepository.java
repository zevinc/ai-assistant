package com.aiassistant.modules.memory.domain.repository;

import com.aiassistant.modules.memory.domain.entity.Memory;

import java.util.List;
import java.util.Optional;

/**
 * 记忆仓储接口
 */
public interface MemoryRepository {

    /**
     * 根据ID查找记忆
     *
     * @param id 记忆ID
     * @return 记忆实体
     */
    Optional<Memory> findById(Long id);

    /**
     * 根据会话ID查找所有记忆
     *
     * @param sessionId 会话ID
     * @return 记忆列表
     */
    List<Memory> findBySessionId(String sessionId);

    /**
     * 根据Agent ID查找所有记忆
     *
     * @param agentId Agent ID
     * @return 记忆列表
     */
    List<Memory> findByAgentId(Long agentId);

    /**
     * 根据会话ID和记忆类型查找记忆
     *
     * @param sessionId  会话ID
     * @param memoryType 记忆类型
     * @return 记忆列表
     */
    List<Memory> findBySessionIdAndType(String sessionId, String memoryType);

    /**
     * 保存记忆
     *
     * @param memory 记忆实体
     * @return 保存后的记忆
     */
    Memory save(Memory memory);

    /**
     * 批量保存记忆
     *
     * @param memories 记忆列表
     * @return 保存后的记忆列表
     */
    List<Memory> saveBatch(List<Memory> memories);

    /**
     * 根据会话ID删除记忆
     *
     * @param sessionId 会话ID
     */
    void deleteBySessionId(String sessionId);

    /**
     * 根据ID删除记忆
     *
     * @param id 记忆ID
     */
    void deleteById(Long id);
}
