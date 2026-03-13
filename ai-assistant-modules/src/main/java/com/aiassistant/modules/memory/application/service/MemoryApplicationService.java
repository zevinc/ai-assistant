package com.aiassistant.modules.memory.application.service;

import com.aiassistant.common.event.DomainEventPublisher;
import com.aiassistant.common.event.SimpleDomainEvent;
import com.aiassistant.modules.memory.domain.entity.Memory;
import com.aiassistant.modules.memory.domain.repository.MemoryRepository;
import com.aiassistant.modules.memory.domain.service.MemoryCompressor;
import com.aiassistant.modules.memory.domain.service.MemoryImportanceEvaluator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 记忆应用服务
 * 提供记忆管理的业务用例
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MemoryApplicationService {

    private final MemoryRepository memoryRepository;
    private final MemoryImportanceEvaluator importanceEvaluator;
    private final MemoryCompressor memoryCompressor;
    private final DomainEventPublisher eventPublisher;

    /**
     * 添加记忆
     *
     * @param sessionId  会话ID
     * @param agentId    Agent ID
     * @param role       角色
     * @param content    内容
     * @param memoryType 记忆类型
     * @return 保存的记忆
     */
    @Transactional
    public Memory addMemory(String sessionId, Long agentId, String role, String content, String memoryType) {
        log.info("Adding memory for session: {}, agent: {}", sessionId, agentId);

        // 评估重要性
        int importance = importanceEvaluator.evaluate(content);

        // 计算token数量（简化实现，按字符数估算）
        int tokenCount = content != null ? content.length() / 4 : 0;

        Memory memory = Memory.builder()
                .sessionId(sessionId)
                .agentId(agentId)
                .role(role)
                .content(content)
                .importance(importance)
                .tokenCount(tokenCount)
                .memoryType(memoryType)
                .build();

        Memory savedMemory = memoryRepository.save(memory);

        // 发布记忆创建事件
        eventPublisher.publish(new SimpleDomainEvent(sessionId, "Memory", "MemoryCreated"));

        log.info("Memory added successfully with id: {}, importance: {}", savedMemory.getId(), importance);
        return savedMemory;
    }

    /**
     * 获取会话的所有记忆
     *
     * @param sessionId 会话ID
     * @return 记忆列表
     */
    public List<Memory> getSessionMemories(String sessionId) {
        log.info("Getting memories for session: {}", sessionId);
        return memoryRepository.findBySessionId(sessionId);
    }

    /**
     * 获取最近的记忆
     *
     * @param sessionId 会话ID
     * @param limit     限制数量
     * @return 记忆列表
     */
    public List<Memory> getRecentMemories(String sessionId, int limit) {
        log.info("Getting recent {} memories for session: {}", limit, sessionId);
        List<Memory> memories = memoryRepository.findBySessionId(sessionId);
        return memories.stream()
                .limit(limit)
                .toList();
    }

    /**
     * 压缩旧记忆
     *
     * @param sessionId 会话ID
     * @return 压缩后的摘要
     */
    @Transactional
    public String compressOldMemories(String sessionId) {
        log.info("Compressing old memories for session: {}", sessionId);

        List<Memory> memories = memoryRepository.findBySessionId(sessionId);
        String summary = memoryCompressor.compress(memories);

        // 创建一条摘要记忆
        Memory summaryMemory = Memory.builder()
                .sessionId(sessionId)
                .role("system")
                .content(summary)
                .memoryType("semantic")
                .importance(8)
                .build();

        memoryRepository.save(summaryMemory);

        log.info("Old memories compressed for session: {}", sessionId);
        return summary;
    }

    /**
     * 清除会话记忆
     *
     * @param sessionId 会话ID
     */
    @Transactional
    public void clearSession(String sessionId) {
        log.info("Clearing memories for session: {}", sessionId);
        memoryRepository.deleteBySessionId(sessionId);

        // 发布会话清除事件
        eventPublisher.publish(new SimpleDomainEvent(sessionId, "MemorySession", "SessionCleared"));

        log.info("Session memories cleared: {}", sessionId);
    }
}
