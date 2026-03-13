package com.aiassistant.modules.prompt.domain.service;

import com.aiassistant.modules.prompt.domain.entity.PromptTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Prompt渲染器
 * 用于渲染Prompt模板，替换变量占位符
 */
@Service
@Slf4j
public class PromptRenderer {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{(\\w+)}}");

    /**
     * 渲染Prompt模板
     * 将模板中的{{variable}}占位符替换为实际值
     *
     * @param template  Prompt模板
     * @param variables 变量映射
     * @return 渲染后的字符串
     */
    public String render(PromptTemplate template, Map<String, Object> variables) {
        log.debug("Rendering prompt template: {}", template.getName());

        if (template == null || template.getTemplate() == null) {
            return "";
        }

        String result = template.getTemplate();

        if (variables == null || variables.isEmpty()) {
            return result;
        }

        Matcher matcher = VARIABLE_PATTERN.matcher(result);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String variableName = matcher.group(1);
            Object value = variables.get(variableName);

            String replacement;
            if (value != null) {
                replacement = escapeReplacement(value.toString());
            } else {
                log.warn("Variable '{}' not found in template '{}', keeping placeholder", variableName, template.getName());
                replacement = matcher.group(0); // 保持原占位符
            }

            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);

        String renderedResult = sb.toString();
        log.debug("Prompt rendered successfully, original length: {}, rendered length: {}",
                result.length(), renderedResult.length());

        return renderedResult;
    }

    /**
     * 渲染字符串模板
     * 将模板中的{{variable}}占位符替换为实际值
     *
     * @param template  模板字符串
     * @param variables 变量映射
     * @return 渲染后的字符串
     */
    public String render(String template, Map<String, Object> variables) {
        if (template == null || template.isEmpty()) {
            return "";
        }

        if (variables == null || variables.isEmpty()) {
            return template;
        }

        Matcher matcher = VARIABLE_PATTERN.matcher(template);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String variableName = matcher.group(1);
            Object value = variables.get(variableName);

            String replacement;
            if (value != null) {
                replacement = escapeReplacement(value.toString());
            } else {
                replacement = matcher.group(0);
            }

            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    /**
     * 转义替换字符串中的特殊字符
     *
     * @param str 原始字符串
     * @return 转义后的字符串
     */
    private String escapeReplacement(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                .replace("$", "\\$");
    }

    /**
     * 验证模板变量是否完整
     *
     * @param template  Prompt模板
     * @param variables 变量映射
     * @return 是否完整
     */
    public boolean validateVariables(PromptTemplate template, Map<String, Object> variables) {
        if (template == null || template.getTemplate() == null) {
            return true;
        }

        Matcher matcher = VARIABLE_PATTERN.matcher(template.getTemplate());
        while (matcher.find()) {
            String variableName = matcher.group(1);
            if (variables == null || !variables.containsKey(variableName)) {
                return false;
            }
        }
        return true;
    }
}
