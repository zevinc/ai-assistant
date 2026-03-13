package com.aiassistant.modules.wiki.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 知识库视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WikiVO {

    /**
     * 知识库ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 内容摘要（前200字符）
     */
    private String contentSummary;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签
     */
    private String tags;

    /**
     * 来源类型
     */
    private String sourceType;

    /**
     * 来源类型描述
     */
    private String sourceTypeDesc;

    /**
     * 来源URL
     */
    private String sourceUrl;

    /**
     * 关联的规格ID
     */
    private Long specId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 分块数量
     */
    private Long chunkCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
