package com.aiassistant.modules.evaluation.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 评估引擎
 * 提供AI响应质量评估的核心算法
 */
@Service
@Slf4j
public class EvaluationEngine {

    /**
     * 评估输入输出质量
     * 使用字符串相似度算法计算评分
     *
     * @param input    输入文本
     * @param output   输出文本
     * @param expected 期望输出
     * @return 评分（0.0-1.0）
     */
    public double evaluate(String input, String output, String expected) {
        log.debug("Euating output against expected result");

        if (output == null || output.isEmpty()) {
            return 0.0;
        }

        if (expected == null || expected.isEmpty()) {
            // 如果没有期望输出，基于输出长度给予基础评分
            return Math.min(1.0, output.length() / 100.0);
        }

        // 使用Levenshtein距离计算相似度
        double similarity = calculateStringSimilarity(output, expected);

        // 考虑输入相关性（简单实现：检查输出是否包含输入中的关键词）
        double inputRelevance = calculateInputRelevance(input, output);

        // 综合评分：70%相似度 + 30%输入相关性
        double score = similarity * 0.7 + inputRelevance * 0.3;

        log.debug("Euation result - similarity: {}, inputRelevance: {}, final score: {}",
                similarity, inputRelevance, score);

        return Math.round(score * 100.0) / 100.0; // 保留两位小数
    }

    /**
     * 计算字符串相似度（基于Levenshtein距离）
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 相似度（0.0-1.0）
     */
    private double calculateStringSimilarity(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return 0.0;
        }

        String s1 = str1.toLowerCase().trim();
        String s2 = str2.toLowerCase().trim();

        if (s1.equals(s2)) {
            return 1.0;
        }

        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) {
            return 1.0;
        }

        int distance = levenshteinDistance(s1, s2);
        return 1.0 - (double) distance / maxLen;
    }

    /**
     * 计算Levenshtein距离
     *
     * @param s1 字符串1
     * @param s2 字符串2
     * @return 编辑距离
     */
    private int levenshteinDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j], dp[i][j - 1]),
                            dp[i - 1][j - 1]
                    ) + 1;
                }
            }
        }

        return dp[m][n];
    }

    /**
     * 计算输入相关性
     * 检查输出是否包含输入中的关键词
     *
     * @param input  输入文本
     * @param output 输出文本
     * @return 相关性评分（0.0-1.0）
     */
    private double calculateInputRelevance(String input, String output) {
        if (input == null || input.isEmpty() || output == null || output.isEmpty()) {
            return 0.5; // 默认中等相关性
        }

        // 提取输入中的关键词（简单分词）
        String[] keywords = input.toLowerCase().split("\\s+");
        if (keywords.length == 0) {
            return 0.5;
        }

        String outputLower = output.toLowerCase();
        int matchCount = 0;

        for (String keyword : keywords) {
            if (keyword.length() > 2 && outputLower.contains(keyword)) {
                matchCount++;
            }
        }

        return Math.min(1.0, (double) matchCount / keywords.length);
    }
}
