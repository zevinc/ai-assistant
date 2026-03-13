package com.aiassistant.modules.memory.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.memory.application.service.MemoryApplicationService;
import com.aiassistant.modules.memory.domain.entity.Memory;
import com.aiassistant.modules.memory.interfaces.dto.MemoryCreateRequest;
import com.aiassistant.modules.memory.interfaces.dto.MemoryQueryRequest;
import com.aiassistant.modules.memory.interfaces.vo.MemoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 记忆控制器
 * 提供记忆管理的REST API
 */
@RestController
@RequestMapping("/api/v1/memories")
@Slf4j
@RequiredArgsConstructor
public class MemoryController {

    private final MemoryApplicationService memoryApplicationService;

    /**
     * 添加记忆
     *
     * @param request 记忆创建请求
     * @return 创建的记忆
     */
    @PostMapping
    public Result<MemoryVO> add(@RequestBody MemoryCreateRequest request) {
        log.info("Adding memory for session: {}", request.getSessionId());

        Memory memory = memoryApplicationService.addMemory(
                request.getSessionId(),
                request.getAgentId(),
                request.getRole(),
                request.getContent(),
                request.getMemoryType()
        );

        return Result.ok(toVO(memory));
    }

    /**
     * 查询记忆
     *
     * @param request 查询请求
     * @return 记忆列表
     */
    @GetMapping
    public Result<List<MemoryVO>> query(MemoryQueryRequest request) {
        log.info("Querying memories for session: {}", request.getSessionId());

        List<Memory> memories;
        if (request.getLimit() != null && request.getLimit() > 0) {
            memories = memoryApplicationService.getRecentMemories(
                    request.getSessionId(),
                    request.getLimit()
            );
        } else {
            memories = memoryApplicationService.getSessionMemories(request.getSessionId());
        }

        List<MemoryVO> voList = memories.stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return Result.ok(voList);
    }

    /**
     * 压缩旧记忆
     *
     * @param sessionId 会话ID
     * @return 压缩后的摘要
     */
    @PostMapping("/{sessionId}/compress")
    public Result<String> compress(@PathVariable String sessionId) {
        log.info("Compressing memories for session: {}", sessionId);
        String summary = memoryApplicationService.compressOldMemories(sessionId);
        return Result.ok(summary);
    }

    /**
     * 清除会话记忆
     *
     * @param sessionId 会话ID
     * @return 操作结果
     */
    @DeleteMapping("/{sessionId}")
    public Result<Void> clear(@PathVariable String sessionId) {
        log.info("Clearing memories for session: {}", sessionId);
        memoryApplicationService.clearSession(sessionId);
        return Result.ok();
    }

    /**
     * 将实体转换为VO
     */
    private MemoryVO toVO(Memory memory) {
        return MemoryVO.builder()
                .id(memory.getId())
                .sessionId(memory.getSessionId())
                .agentId(memory.getAgentId())
                .role(memory.getRole())
                .content(memory.getContent())
                .summary(memory.getSummary())
                .importance(memory.getImportance())
                .tokenCount(memory.getTokenCount())
                .memoryType(memory.getMemoryType())
                .metadata(memory.getMetadata())
                .createdAt(memory.getCreatedAt())
                .build();
    }
}
