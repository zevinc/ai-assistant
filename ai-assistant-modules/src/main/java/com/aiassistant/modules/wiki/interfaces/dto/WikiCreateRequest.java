package com.aiassistant.modules.wiki.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 知识库创建请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WikiCreateRequest {

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签（逗号分隔）
     */
    private String tags;

    /**
     * 来源类型
     */
    private String sourceType;

    /**
     * 来源URL
     */
    private String sourceUrl;

    /**
     * 关联的规格ID
     */
    private Long specId;
}
