package com.aiassistant.modules.wiki.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 知识库分块实体
 * 将知识库内容分割成小块用于向量检索
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_wiki_chunk")
public class WikiChunk extends BaseEntity {

    /**
     * 关联的知识库ID
     */
    private Long wikiId;

    /**
     * 分块索引（从0开始）
     */
    private Integer chunkIndex;

    /**
     * 分块内容
     */
    private String content;

    /**
     * 向量嵌入（JSON格式存储）
     */
    private String embedding;

    /**
     * 令牌数量
     */
    private Integer tokenCount;
}
