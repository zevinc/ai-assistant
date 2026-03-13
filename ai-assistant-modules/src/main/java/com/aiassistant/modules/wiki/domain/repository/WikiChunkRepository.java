package com.aiassistant.modules.wiki.domain.repository;

import com.aiassistant.modules.wiki.domain.entity.WikiChunk;

import java.util.List;

/**
 * 知识库分块仓储接口
 * 定义知识库分块实体的持久化操作
 */
public interface WikiChunkRepository {

    /**
     * 根据知识库ID查找所有分块
     *
     * @param wikiId 知识库ID
     * @return 分块列表
     */
    List<WikiChunk> findByWikiId(Long wikiId);

    /**
     * 根据知识库ID和索引范围查找分块
     *
     * @param wikiId 知识库ID
     * @param startIndex 起始索引
     * @param endIndex 结束索引
     * @return 分块列表
     */
    List<WikiChunk> findByWikiIdAndIndexRange(Long wikiId, int startIndex, int endIndex);

    /**
     * 根据ID查找分块
     *
     * @param id 分块ID
     * @return 分块实体
     */
    WikiChunk findById(Long id);

    /**
     * 保存单个分块
     *
     * @param chunk 分块实体
     * @return 保存后的分块
     */
    WikiChunk save(WikiChunk chunk);

    /**
     * 批量保存分块
     *
     * @param chunks 分块列表
     * @return 保存成功的数量
     */
    int saveChunks(List<WikiChunk> chunks);

    /**
     * 更新分块
     *
     * @param chunk 分块实体
     * @return 更新后的分块
     */
    WikiChunk update(WikiChunk chunk);

    /**
     * 根据ID删除分块
     *
     * @param id 分块ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 根据知识库ID删除所有分块
     *
     * @param wikiId 知识库ID
     * @return 删除数量
     */
    int deleteByWikiId(Long wikiId);

    /**
     * 统计知识库的分块数量
     *
     * @param wikiId 知识库ID
     * @return 分块数量
     */
    long countByWikiId(Long wikiId);

    /**
     * 根据内容模糊搜索分块
     *
     * @param keyword 关键词
     * @param limit 返回数量限制
     * @return 分块列表
     */
    List<WikiChunk> searchByContent(String keyword, int limit);
}
