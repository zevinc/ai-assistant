package com.aiassistant.modules.wiki.application.service;

import com.aiassistant.common.event.DomainEventPublisher;
import com.aiassistant.common.event.SimpleDomainEvent;
import com.aiassistant.modules.wiki.domain.entity.Wiki;
import com.aiassistant.modules.wiki.domain.entity.WikiChunk;
import com.aiassistant.modules.wiki.domain.enums.WikiSourceTypeEnum;
import com.aiassistant.modules.wiki.domain.repository.WikiChunkRepository;
import com.aiassistant.modules.wiki.domain.repository.WikiRepository;
import com.aiassistant.modules.wiki.domain.service.WikiIndexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 知识库应用服务
 * 提供知识库的CRUD操作和索引管理
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WikiApplicationService {

    private final WikiRepository wikiRepository;
    private final WikiChunkRepository wikiChunkRepository;
    private final WikiIndexService wikiIndexService;
    private final DomainEventPublisher domainEventPublisher;

    /**
     * 创建知识库
     *
     * @param wiki 知识库实体
     * @return 创建后的知识库
     */
    @Transactional(rollbackFor = Exception.class)
    public Wiki create(Wiki wiki) {
        // 验证知识库
        validateWiki(wiki);

        // 检查标题是否已存在
        if (wikiRepository.existsByTitle(wiki.getTitle())) {
            throw new IllegalArgumentException("知识库标题已存在: " + wiki.getTitle());
        }

        // 设置默认状态
        if (wiki.getStatus() == null) {
            wiki.setStatus(1); // 默认启用
        }

        // 设置默认来源类型
        if (wiki.getSourceType() == null) {
            wiki.setSourceType(WikiSourceTypeEnum.MANUAL.getCode());
        }

        // 保存知识库
        Wiki savedWiki = wikiRepository.save(wiki);

        // 创建索引
        try {
            wikiIndexService.indexWiki(savedWiki);
        } catch (Exception e) {
            log.warn("创建知识库索引失败: ID={}", savedWiki.getId(), e);
        }

        // 发布领域事件
        publishEvent(new SimpleDomainEvent(String.valueOf(savedWiki.getId()), "Wiki", "wiki.created"));

        log.info("创建知识库成功: {} (ID: {})", savedWiki.getTitle(), savedWiki.getId());
        return savedWiki;
    }

    /**
     * 更新知识库
     *
     * @param wiki 知识库实体
     * @return 更新后的知识库
     */
    @Transactional(rollbackFor = Exception.class)
    public Wiki update(Wiki wiki) {
        // 检查知识库是否存在
        Wiki existingWiki = wikiRepository.findById(wiki.getId())
                .orElseThrow(() -> new IllegalArgumentException("知识库不存在: " + wiki.getId()));

        // 验证知识库
        validateWiki(wiki);

        // 检查标题是否与其他知识库冲突
        if (!existingWiki.getTitle().equals(wiki.getTitle()) &&
                wikiRepository.existsByTitle(wiki.getTitle())) {
            throw new IllegalArgumentException("知识库标题已存在: " + wiki.getTitle());
        }

        // 保留不可修改的字段
        wiki.setCreatedAt(existingWiki.getCreatedAt());
        wiki.setCreatedBy(existingWiki.getCreatedBy());

        // 更新知识库
        Wiki updatedWiki = wikiRepository.update(wiki);

        // 如果内容有变化，重新创建索引
        if (!existingWiki.getContent().equals(wiki.getContent())) {
            try {
                wikiIndexService.indexWiki(updatedWiki);
            } catch (Exception e) {
                log.warn("更新知识库索引失败: ID={}", updatedWiki.getId(), e);
            }
        }

        // 发布领域事件
        publishEvent(new SimpleDomainEvent(String.valueOf(updatedWiki.getId()), "Wiki", "wiki.updated"));

        log.info("更新知识库成功: {} (ID: {})", updatedWiki.getTitle(), updatedWiki.getId());
        return updatedWiki;
    }

    /**
     * 根据ID查询知识库
     *
     * @param id 知识库ID
     * @return 知识库实体
     */
    public Optional<Wiki> findById(Long id) {
        return wikiRepository.findById(id);
    }

    /**
     * 根据规格ID查询知识库列表
     *
     * @param specId 规格ID
     * @return 知识库列表
     */
    public List<Wiki> findBySpecId(Long specId) {
        return wikiRepository.findBySpecId(specId);
    }

    /**
     * 根据分类查询知识库列表
     *
     * @param category 分类
     * @return 知识库列表
     */
    public List<Wiki> findByCategory(String category) {
        return wikiRepository.findByCategory(category);
    }

    /**
     * 查询所有知识库
     *
     * @return 知识库列表
     */
    public List<Wiki> listAll() {
        return wikiRepository.listAll();
    }

    /**
     * 删除知识库
     *
     * @param id 知识库ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        // 检查知识库是否存在
        Wiki wiki = wikiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("知识库不存在: " + id));

        // 删除知识库分块
        wikiChunkRepository.deleteByWikiId(id);

        // 删除知识库
        wikiRepository.deleteById(id);

        // 发布领域事件
        publishEvent(new SimpleDomainEvent(String.valueOf(id), "Wiki", "wiki.deleted"));

        log.info("删除知识库成功: {} (ID: {})", wiki.getTitle(), id);
    }

    /**
     * 重新索引知识库
     *
     * @param wikiId 知识库ID
     * @return 创建的分块列表
     */
    @Transactional(rollbackFor = Exception.class)
    public List<WikiChunk> reindex(Long wikiId) {
        Wiki wiki = wikiRepository.findById(wikiId)
                .orElseThrow(() -> new IllegalArgumentException("知识库不存在: " + wikiId));

        log.info("重新索引知识库: ID={}", wikiId);

        return wikiIndexService.indexWiki(wiki);
    }

    /**
     * 获取知识库的分块列表
     *
     * @param wikiId 知识库ID
     * @return 分块列表
     */
    public List<WikiChunk> getChunks(Long wikiId) {
        return wikiChunkRepository.findByWikiId(wikiId);
    }

    /**
     * 启用知识库
     *
     * @param id 知识库ID
     * @return 更新后的知识库
     */
    @Transactional(rollbackFor = Exception.class)
    public Wiki enable(Long id) {
        Wiki wiki = wikiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("知识库不存在: " + id));

        wiki.setStatus(1);
        Wiki updatedWiki = wikiRepository.update(wiki);

        log.info("启用知识库成功: {} (ID: {})", wiki.getTitle(), id);
        return updatedWiki;
    }

    /**
     * 禁用知识库
     *
     * @param id 知识库ID
     * @return 更新后的知识库
     */
    @Transactional(rollbackFor = Exception.class)
    public Wiki disable(Long id) {
        Wiki wiki = wikiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("知识库不存在: " + id));

        wiki.setStatus(0);
        Wiki updatedWiki = wikiRepository.update(wiki);

        log.info("禁用知识库成功: {} (ID: {})", wiki.getTitle(), id);
        return updatedWiki;
    }

    /**
     * 验证知识库
     *
     * @param wiki 知识库实体
     */
    private void validateWiki(Wiki wiki) {
        if (wiki == null) {
            throw new IllegalArgumentException("知识库不能为空");
        }

        if (wiki.getTitle() == null || wiki.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("知识库标题不能为空");
        }

        if (wiki.getContent() == null || wiki.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("知识库内容不能为空");
        }

        if (wiki.getSourceType() != null && !WikiSourceTypeEnum.isValidCode(wiki.getSourceType())) {
            throw new IllegalArgumentException("无效的来源类型: " + wiki.getSourceType());
        }
    }

    /**
     * 发布领域事件
     *
     * @param event 领域事件
     */
    private void publishEvent(SimpleDomainEvent event) {
        try {
            domainEventPublisher.publish(event);
        } catch (Exception e) {
            log.warn("发布领域事件失败: {}", event, e);
        }
    }
}
