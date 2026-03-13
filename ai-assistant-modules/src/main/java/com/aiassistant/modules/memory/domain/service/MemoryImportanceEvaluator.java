package com.aiassistant.modules.memory.domain.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 记忆重要性评估服务
 * 根据内容特征评估记忆的重要性评分
 */
@Service
public class MemoryImportanceEvaluator {

    /**
     * 高重要性关键词集合
     */
    private static final Set<String> HIGH_IMPORTANCE_KEYWORDS = new HashSet<>(Arrays.asList(
            "important", "critical", "urgent", "key", "essential", "must", "remember",
            "重要", "关键", "紧急", "必须", "记住", "核心", "注意"
    ));

    /**
     * 中等重要性关键词集合
     */
    private static final Set<String> MEDIUM_IMPORTANCE_KEYWORDS = new HashSet<>(Arrays.asList(
            "need", "should", "want", "prefer", "like", "think",
            "需要", "应该", "想要", "喜欢", "认为", "觉得"
    ));

    /**
     * 评估记忆内容的重要性
     *
     * @param content 记忆内容
     * @return 重要性评分（1-10）
     */
    public int evaluate(String content) {
        if (content == null || content.trim().isEmpty()) {
            return 1;
        }

        int score = 5; // 基础分

        // 基于内容长度调整评分
        int length = content.length();
        if (length > 500) {
            score += 2;
        } else if (length > 200) {
            score += 1;
        } else if (length < 50) {
            score -= 1;
        }

        // 基于关键词调整评分
        String lowerContent = content.toLowerCase();
        for (String keyword : HIGH_IMPORTANCE_KEYWORDS) {
            if (lowerContent.contains(keyword.toLowerCase())) {
                score += 2;
                break;
            }
        }

        for (String keyword : MEDIUM_IMPORTANCE_KEYWORDS) {
            if (lowerContent.contains(keyword.toLowerCase())) {
                score += 1;
                break;
            }
        }

        // 检查是否包含问号（可能是重要问题）
        if (content.contains("?") || content.contains("？")) {
            score += 1;
        }

        // 检查是否包含数字或日期（可能是具体信息）
        if (content.matches(".*\\d+.*")) {
            score += 1;
        }

        // 限制评分范围在1-10之间
        return Math.max(1, Math.min(10, score));
    }
}
