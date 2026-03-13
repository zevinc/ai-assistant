package com.aiassistant.modules.wiki.domain.service;

import com.aiassistant.core.embedding.service.TextSplitter;
import com.aiassistant.modules.wiki.domain.entity.Wiki;
import com.aiassistant.modules.wiki.domain.entity.WikiChunk;
import com.aiassistant.modules.wiki.domain.repository.WikiChunkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识库索引服务
 * 负责将知识库内容分割成块并建立索引
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WikiIndexService {

    private final WikiChunkRepository wikiChunkRepository;
    private final TextSplitter textSplitter;

    /**
     * 为知识库创建索引
     * 将内容分割成块并保存
     *
     * @param wiki 知识库实体
     * @return 创建的分块列表
     */
    public List<WikiChunk> indexWiki(Wiki wiki) {
        if (wiki == null || wiki.getId() == null) {
            throw new IllegalArgumentException("知识库或知识库ID不能为空");
        }

        if (wiki.getContent() == null || wiki.getContent().trim().isEmpty()) {
            log.warn("知识库内容为空，跳过索引: ID={}", wiki.getId());
            return List.of();
        }

        log.info("开始为知识库创建索引: ID={}, 标题={}", wiki.getId(), wiki.getTitle());

        // 先删除旧的分块
        wikiChunkRepository.deleteByWikiId(wiki.getId());

        // 使用TextSplitter分割内容
        List<String> chunks = textSplitter.split(wiki.getContent());
        log.debug("内容分割完成，共{}个分块", chunks.size());

        // 创建分块实体
        List<WikiChunk> wikiChunks = new ArrayList<>();
        for (int i = 0; i < chunks.size(); i++) {
            String chunkContent = chunks.get(i);

            WikiChunk chunk = WikiChunk.builder()
                    .wikiId(wiki.getId())
                    .chunkIndex(i)
                    .content(chunkContent)
                    .tokenCount(estimateTokenCount(chunkContent))
                    .build();

            wikiChunks.add(chunk);
        }

        // 批量保存分块
        int savedCount = wikiChunkRepository.saveChunks(wikiChunks);
        log.info("知识库索引创建完成: ID={}, 分块数量={}", wiki.getId(), savedCount);

        return wikiChunks;
    }

    /**
     * 重新索引知识库
     *
     * @param wikiId 知识库ID
     * @return 创建的分块列表
     */
    public List<WikiChunk> reindex(Long wikiId) {
        // 注意：这里需要WikiRepository来获取Wiki实体
        // 由于这是领域服务，不应该直接依赖应用层
        // 实际使用时应该由应用服务调用indexWiki方法
        log.info("重新索引知识库: ID={}", wikiId);

        // 删除旧分块
        wikiChunkRepository.deleteByWikiId(wikiId);

        log.info("知识库分块已清除: ID={}", wikiId);
        return List.of();
    }

    /**
     * 删除知识库索引
     *
     * @param wikiId 知识库ID
     * @return 删除的分块数量
     */
    public int deleteIndex(Long wikiId) {
        log.info("删除知识库索引: ID={}", wikiId);
        return wikiChunkRepository.deleteByWikiId(wikiId);
    }

    /**
     * 获取知识库的分块信息
     *
     * @param wikiId 知识库ID
     * @return 分块列表
     */
    public List<WikiChunk> getChunks(Long wikiId) {
        return wikiChunkRepository.findByWikiId(wikiId);
    }

    /**
     * 统计知识库的分块数量
     *
     * @param wikiId 知识库ID
     * @return 分块数量
     */
    public long getChunkCount(Long wikiId) {
        return wikiChunkRepository.countByWikiId(wikiId);
    }

    /**
     * 估算令牌数量
     * 简单估算：中文按字符数，英文按单词数*1.3
     *
     * @param text 文本
     * @return 估算的令牌数量
     */
    private int estimateTokenCount(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }

        // 简单估算：字符数 * 0.5（粗略估计）
        // 实际项目中应该使用tiktoken等库进行精确计算
        int charCount = text.length();
        int chineseCount = 0;
        int englishWordCount = 0;

        boolean inWord = false;
        for (char c : text.toCharArray()) {
            if (Character.toString(c).matches("[\\u4e00-\\u9fa5]")) {
                chineseCount++;
                inWord = false;
            } else if (Character.isLetter(c)) {
                if (!inWord) {
                    englishWordCount++;
                    inWord = true;
                }
            } else {
                inWord = false;
            }
        }

        // 中文约1.5字符/token，英文约0.75单词/token
        return (int) Math.ceil(chineseCount * 0.7 + englishWordCount * 1.3);
    }
}
