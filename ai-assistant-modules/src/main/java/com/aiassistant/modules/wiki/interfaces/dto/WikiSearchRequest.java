package com.aiassistant.modules.wiki.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 知识库搜索请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WikiSearchRequest {

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 分类
     */
    private String category;

    /**
     * 规格ID
     */
    private Long specId;

    /**
     * 页码（从0开始）
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 获取页码，默认为0
     */
    public int getPageNum() {
        return pageNum != null ? pageNum : 0;
    }

    /**
     * 获取每页大小，默认为10
     */
    public int getPageSize() {
        return pageSize != null ? pageSize : 10;
    }
}
