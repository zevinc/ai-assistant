package com.aiassistant.modules.prompt.interfaces.dto;

import lombok.Data;

import java.util.Map;

/**
 * Prompt渲染请求
 */
@Data
public class PromptRenderRequest {

    /**
     * 模板ID
     */
    private Long templateId;

    /**
     * 变量映射
     */
    private Map<String, Object> variables;
}
