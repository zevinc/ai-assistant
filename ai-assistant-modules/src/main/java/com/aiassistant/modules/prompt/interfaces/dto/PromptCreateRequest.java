package com.aiassistant.modules.prompt.interfaces.dto;

import lombok.Data;

/**
 * Prompt创建请求
 */
@Data
public class PromptCreateRequest {

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板描述
     */
    private String description;

    /**
     * 分类
     */
    private String category;

    /**
     * 模板内容
     */
    private String template;

    /**
     * 变量列表（JSON数组格式）
     */
    private String variables;

    /**
     * 规格ID
     */
    private Long specId;
}
