package com.aiassistant.modules.wiki.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 知识库实体
 * 用于RAG检索增强生成的知识库条目
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_wiki")
public class Wiki extends BaseEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
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
     * 来源类型：manual, url, file, api
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

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}
