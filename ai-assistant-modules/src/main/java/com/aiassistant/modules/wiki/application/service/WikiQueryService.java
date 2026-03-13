package com.aiassistant.modules.wiki.application.service;

import com.aiassistant.modules.wiki.domain.entity.Wiki;
import com.aiassistant.modules.wiki.domain.entity.WikiChunk;
import com.aiassistant.modules.wiki.domain.repository.WikiChunkRepository;
import com.aiassistant.modules.wiki.domain.repository.WikiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 知识库查询服务
 * CQRS模式下的查询端服务，专注于读取操作
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WikiQueryService {

    private final WikiRepository wikiRepository;
    private final WikiChunkRepository wikiChunkRepository;

    /**
     * 搜索知识库
     *
     * @param keyword 关键词
     * @return 匹配的知识库列表
     */
    public List<Wiki> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return wikiRepository.listAll();
        }

        log.debug("搜索知识库: keyword={}", keyword);
        return wikiRepository.search(keyword);
    }

    /**
     * 根据分类搜索知识库
     *
     * @param category 分类
     * @return 知识库列表
     */
    public List<Wiki> findByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return List.of();
        }

        log.debug("按分类查询知识库: category={}", category);
        return wikiRepository.findByCategory(category);
    }

    /**
     * 分页搜索知识库
     *
     * @param keyword 关键词
     * @param pageNum 页码（从0开始）
     * @param pageSize 每页大小
     * @return 分页结果
     */
    public Page<Wiki> searchPaged(String keyword, int pageNum, int pageSize) {
        List<Wiki> allResults = search(keyword);

        // 创建分页请求
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        // 计算分页
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allResults.size());

        List<Wiki> pageContent = allResults.subList(start, end);

        return new PageImpl<>(pageContent, pageable, allResults.size());
    }

    /**
     * 根据分类和关键词搜索
     *
     * @param keyword 关键词
     * @param category 分类
     * @param specId 规格ID（可选）
     * @return 知识库列表
     */
    public List<Wiki> searchByKeywordAndCategory(String keyword, String category, Long specId) {
        log.debug("搜索知识库: keyword={}, category={}, specId={}", keyword, category, specId);

        List<Wiki> results;

        if (keyword != null && !keyword.trim().isEmpty()) {
            results = wikiRepository.search(keyword);
        } else if (category != null && !category.trim().isEmpty()) {
            results = wikiRepository.findByCategory(category);
        } else if (specId != null) {
            results = wikiRepository.findBySpecId(specId);
        } else {
            results = wikiRepository.listAll();
        }

        // 应用过滤条件
        return results.stream()
                .filter(wiki -> category == null || category.trim().isEmpty() ||
                        category.equals(wiki.getCategory()))
                .filter(wiki -> specId == null || specId.equals(wiki.getSpecId()))
                .filter(wiki -> wiki.getStatus() != null && wiki.getStatus() == 1) // 只返回启用的
                .collect(Collectors.toList());
    }

    /**
     * 搜索知识库分块
     *
     * @param keyword 关键词
     * @param limit 返回数量限制
     * @return 匹配的分块列表
     */
    public List<WikiChunk> searchChunks(String keyword, int limit) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        log.debug("搜索知识库分块: keyword={}, limit={}", keyword, limit);
        return wikiChunkRepository.searchByContent(keyword, limit);
    }

    /**
     * 获取知识库的上下文内容
     * 用于RAG检索，返回与关键词相关的知识库内容
     *
     * @param keyword 关键词
     * @param specId 规格ID（可选，用于限定范围）
     * @param maxChunks 最大返回分块数
     * @return 相关内容文本
     */
    public String getContextForRag(String keyword, Long specId, int maxChunks) {
        log.debug("获取RAG上下文: keyword={}, specId={}, maxChunks={}", keyword, specId, maxChunks);

        List<WikiChunk> chunks;

        if (specId != null) {
            // 先获取该规格下的知识库ID列表
            List<Wiki> wikis = wikiRepository.findBySpecId(specId);
            List<Long> wikiIds = wikis.stream()
                    .filter(w -> w.getStatus() != null && w.getStatus() == 1)
                    .map(Wiki::getId)
                    .collect(Collectors.toList());

            // 搜索这些知识库的分块
            chunks = wikiChunkRepository.searchByContent(keyword, maxChunks).stream()
                    .filter(chunk -> wikiIds.contains(chunk.getWikiId()))
                    .limit(maxChunks)
                    .collect(Collectors.toList());
        } else {
            chunks = wikiChunkRepository.searchByContent(keyword, maxChunks);
        }

        // 拼接内容
        StringBuilder context = new StringBuilder();
        for (int i = 0; i < chunks.size(); i++) {
            if (i > 0) {
                context.append("\n\n---\n\n");
            }
            context.append(chunks.get(i).getContent());
        }

        return context.toString();
    }

    /**
     * 统计知识库数量
     *
     * @return 知识库总数
     */
    public long countAll() {
        return wikiRepository.listAll().size();
    }

    /**
     * 统计指定分类的知识库数量
     *
     * @param category 分类
     * @return 知识库数量
     */
    public long countByCategory(String category) {
        return wikiRepository.findByCategory(category).size();
    }

    /**
     * 统计知识库的分块总数
     *
     * @param wikiId 知识库ID
     * @return 分块数量
     */
    public long countChunks(Long wikiId) {
        return wikiChunkRepository.countByWikiId(wikiId);
    }
}
