package com.aiassistant.modules.prompt.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Prompt模板视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptTemplateVO {

    /**
     * 模板ID
     */
    private Long id;

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
     * 变量列表
     */
    private String variables;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 规格ID
     */
    private Long specId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
