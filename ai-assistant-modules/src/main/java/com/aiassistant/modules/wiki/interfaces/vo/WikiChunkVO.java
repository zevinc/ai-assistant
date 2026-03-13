package com.aiassistant.modules.wiki.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 知识库分块视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WikiChunkVO {

    /**
     * 分块ID
     */
    private Long id;

    /**
     * 知识库ID
     */
    private Long wikiId;

    /**
     * 分块索引
     */
    private Integer chunkIndex;

    /**
     * 分块内容
     */
    private String content;

    /**
     * 内容摘要
     */
    private String contentSummary;

    /**
     * 令牌数量
     */
    private Integer tokenCount;

    /**
     * 是否有嵌入向量
     */
    private Boolean hasEmbedding;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
