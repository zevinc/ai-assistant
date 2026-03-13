package com.aiassistant.modules.memory.domain.service;

import com.aiassistant.modules.memory.domain.entity.Memory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 记忆压缩服务
 * 用于压缩和总结历史记忆
 */
@Service
public class MemoryCompressor {

    /**
     * 压缩记忆列表，生成摘要
     *
     * @param memories 记忆列表
     * @return 压缩后的摘要字符串
     */
    public String compress(List<Memory> memories) {
        if (memories == null || memories.isEmpty()) {
            return "";
        }

        StringBuilder summary = new StringBuilder();
        summary.append("会话摘要:\n");

        // 按重要性排序，优先保留重要记忆
        List<Memory> sortedMemories = memories.stream()
                .sorted((m1, m2) -> {
                    int importance1 = m1.getImportance() != null ? m1.getImportance() : 5;
                    int importance2 = m2.getImportance() != null ? m2.getImportance() : 5;
                    return Integer.compare(importance2, importance1);
                })
                .collect(Collectors.toList());

        // 提取关键信息
        int userMessageCount = 0;
        int assistantMessageCount = 0;
        int totalTokens = 0;

        for (Memory memory : sortedMemories) {
            if ("user".equals(memory.getRole())) {
                userMessageCount++;
            } else if ("assistant".equals(memory.getRole())) {
                assistantMessageCount++;
            }
            if (memory.getTokenCount() != null) {
                totalTokens += memory.getTokenCount();
            }
        }

        summary.append(String.format("- 用户消息数: %d\n", userMessageCount));
        summary.append(String.format("- 助手回复数: %d\n", assistantMessageCount));
        summary.append(String.format("- 总Token数: %d\n", totalTokens));

        // 添加最近的几条重要记忆内容
        summary.append("\n关键对话:\n");
        int count = 0;
        for (Memory memory : sortedMemories) {
            if (count >= 5) {
                break;
            }
            String role = "user".equals(memory.getRole()) ? "用户" : "助手";
            String content = memory.getContent();
            if (content != null && content.length() > 100) {
                content = content.substring(0, 100) + "...";
            }
            summary.append(String.format("[%s] %s\n", role, content));
            count++;
        }

        return summary.toString();
    }
}
