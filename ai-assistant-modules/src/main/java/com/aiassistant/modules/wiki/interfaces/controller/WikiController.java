package com.aiassistant.modules.wiki.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.wiki.application.service.WikiApplicationService;
import com.aiassistant.modules.wiki.application.service.WikiQueryService;
import com.aiassistant.modules.wiki.domain.entity.Wiki;
import com.aiassistant.modules.wiki.domain.entity.WikiChunk;
import com.aiassistant.modules.wiki.domain.enums.WikiSourceTypeEnum;
import com.aiassistant.modules.wiki.interfaces.dto.WikiCreateRequest;
import com.aiassistant.modules.wiki.interfaces.dto.WikiSearchRequest;
import com.aiassistant.modules.wiki.interfaces.vo.WikiChunkVO;
import com.aiassistant.modules.wiki.interfaces.vo.WikiVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 知识库控制器
 * 提供知识库管理的REST API
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/wikis")
@RequiredArgsConstructor
public class WikiController {

    private final WikiApplicationService wikiApplicationService;
    private final WikiQueryService wikiQueryService;

    /**
     * 创建知识库
     *
     * @param request 创建请求
     * @return 创建后的知识库
     */
    @PostMapping
    public Result<WikiVO> create(@Valid @RequestBody WikiCreateRequest request) {
        log.info("创建知识库请求: {}", request.getTitle());

        Wiki wiki = Wiki.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .tags(request.getTags())
                .sourceType(request.getSourceType())
                .sourceUrl(request.getSourceUrl())
                .specId(request.getSpecId())
                .build();

        Wiki createdWiki = wikiApplicationService.create(wiki);
        return Result.ok(toVO(createdWiki));
    }

    /**
     * 更新知识库
     *
     * @param id 知识库ID
     * @param request 更新请求
     * @return 更新后的知识库
     */
    @PutMapping("/{id}")
    public Result<WikiVO> update(@PathVariable Long id, @Valid @RequestBody WikiCreateRequest request) {
        log.info("更新知识库请求: ID={}", id);

        Wiki wiki = Wiki.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .tags(request.getTags())
                .sourceType(request.getSourceType())
                .sourceUrl(request.getSourceUrl())
                .specId(request.getSpecId())
                .build();
        wiki.setId(id);

        Wiki updatedWiki = wikiApplicationService.update(wiki);
        return Result.ok(toVO(updatedWiki));
    }

    /**
     * 根据ID查询知识库
     *
     * @param id 知识库ID
     * @return 知识库详情
     */
    @GetMapping("/{id}")
    public Result<WikiVO> getById(@PathVariable Long id) {
        log.info("查询知识库: ID={}", id);

        return wikiApplicationService.findById(id)
                .map(wiki -> Result.ok(toVO(wiki)))
                .orElse(Result.fail(404, "知识库不存在"));
    }

    /**
     * 根据规格ID查询知识库列表
     *
     * @param specId 规格ID
     * @return 知识库列表
     */
    @GetMapping("/spec/{specId}")
    public Result<List<WikiVO>> listBySpecId(@PathVariable Long specId) {
        log.info("根据规格ID查询知识库: specId={}", specId);

        List<WikiVO> wikiVOList = wikiApplicationService.findBySpecId(specId).stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(wikiVOList);
    }

    /**
     * 根据分类查询知识库列表
     *
     * @param category 分类
     * @return 知识库列表
     */
    @GetMapping("/category/{category}")
    public Result<List<WikiVO>> listByCategory(@PathVariable String category) {
        log.info("根据分类查询知识库: category={}", category);

        List<WikiVO> wikiVOList = wikiApplicationService.findByCategory(category).stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(wikiVOList);
    }

    /**
     * 查询所有知识库
     *
     * @return 知识库列表
     */
    @GetMapping
    public Result<List<WikiVO>> listAll() {
        log.info("查询所有知识库");

        List<WikiVO> wikiVOList = wikiApplicationService.listAll().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(wikiVOList);
    }

    /**
     * 搜索知识库
     *
     * @param request 搜索请求
     * @return 知识库列表
     */
    @PostMapping("/search")
    public Result<List<WikiVO>> search(@RequestBody WikiSearchRequest request) {
        log.info("搜索知识库: keyword={}, category={}, specId={}",
                request.getKeyword(), request.getCategory(), request.getSpecId());

        List<Wiki> wikis = wikiQueryService.searchByKeywordAndCategory(
                request.getKeyword(),
                request.getCategory(),
                request.getSpecId()
        );

        List<WikiVO> wikiVOList = wikis.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(wikiVOList);
    }

    /**
     * 分页搜索知识库
     *
     * @param request 搜索请求
     * @return 分页结果
     */
    @PostMapping("/search/paged")
    public Result<Page<WikiVO>> searchPaged(@RequestBody WikiSearchRequest request) {
        log.info("分页搜索知识库: keyword={}, pageNum={}, pageSize={}",
                request.getKeyword(), request.getPageNum(), request.getPageSize());

        Page<Wiki> wikiPage = wikiQueryService.searchPaged(
                request.getKeyword(),
                request.getPageNum(),
                request.getPageSize()
        );

        Page<WikiVO> voPage = wikiPage.map(this::toVO);
        return Result.ok(voPage);
    }

    /**
     * 删除知识库
     *
     * @param id 知识库ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("删除知识库: ID={}", id);

        wikiApplicationService.deleteById(id);
        return Result.ok();
    }

    /**
     * 获取知识库分块列表
     *
     * @param id 知识库ID
     * @return 分块列表
     */
    @GetMapping("/{id}/chunks")
    public Result<List<WikiChunkVO>> getChunks(@PathVariable Long id) {
        log.info("获取知识库分块: ID={}", id);

        List<WikiChunk> chunks = wikiApplicationService.getChunks(id);
        List<WikiChunkVO> chunkVOList = chunks.stream()
                .map(this::toChunkVO)
                .collect(Collectors.toList());
        return Result.ok(chunkVOList);
    }

    /**
     * 重新索引知识库
     *
     * @param id 知识库ID
     * @return 操作结果
     */
    @PostMapping("/{id}/reindex")
    public Result<List<WikiChunkVO>> reindex(@PathVariable Long id) {
        log.info("重新索引知识库: ID={}", id);

        List<WikiChunk> chunks = wikiApplicationService.reindex(id);
        List<WikiChunkVO> chunkVOList = chunks.stream()
                .map(this::toChunkVO)
                .collect(Collectors.toList());
        return Result.ok(chunkVOList);
    }

    /**
     * 搜索知识库分块
     *
     * @param keyword 关键词
     * @param limit 返回数量限制
     * @return 分块列表
     */
    @GetMapping("/chunks/search")
    public Result<List<WikiChunkVO>> searchChunks(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "10") int limit) {
        log.info("搜索知识库分块: keyword={}, limit={}", keyword, limit);

        List<WikiChunk> chunks = wikiQueryService.searchChunks(keyword, limit);
        List<WikiChunkVO> chunkVOList = chunks.stream()
                .map(this::toChunkVO)
                .collect(Collectors.toList());
        return Result.ok(chunkVOList);
    }

    /**
     * 启用知识库
     *
     * @param id 知识库ID
     * @return 更新后的知识库
     */
    @PostMapping("/{id}/enable")
    public Result<WikiVO> enable(@PathVariable Long id) {
        log.info("启用知识库: ID={}", id);

        Wiki wiki = wikiApplicationService.enable(id);
        return Result.ok(toVO(wiki));
    }

    /**
     * 禁用知识库
     *
     * @param id 知识库ID
     * @return 更新后的知识库
     */
    @PostMapping("/{id}/disable")
    public Result<WikiVO> disable(@PathVariable Long id) {
        log.info("禁用知识库: ID={}", id);

        Wiki wiki = wikiApplicationService.disable(id);
        return Result.ok(toVO(wiki));
    }

    /**
     * 将实体转换为VO
     *
     * @param wiki 知识库实体
     * @return 知识库VO
     */
    private WikiVO toVO(Wiki wiki) {
        WikiSourceTypeEnum sourceTypeEnum = WikiSourceTypeEnum.getByCode(wiki.getSourceType());
        long chunkCount = wikiQueryService.countChunks(wiki.getId());

        // 生成内容摘要
        String contentSummary = null;
        if (wiki.getContent() != null && wiki.getContent().length() > 200) {
            contentSummary = wiki.getContent().substring(0, 200) + "...";
        } else {
            contentSummary = wiki.getContent();
        }

        return WikiVO.builder()
                .id(wiki.getId())
                .title(wiki.getTitle())
                .content(wiki.getContent())
                .contentSummary(contentSummary)
                .category(wiki.getCategory())
                .tags(wiki.getTags())
                .sourceType(wiki.getSourceType())
                .sourceTypeDesc(sourceTypeEnum != null ? sourceTypeEnum.getDescription() : null)
                .sourceUrl(wiki.getSourceUrl())
                .specId(wiki.getSpecId())
                .status(wiki.getStatus())
                .statusDesc(wiki.getStatus() != null && wiki.getStatus() == 1 ? "启用" : "禁用")
                .chunkCount(chunkCount)
                .createdAt(wiki.getCreatedAt())
                .updatedAt(wiki.getUpdatedAt())
                .build();
    }

    /**
     * 将分块实体转换为VO
     *
     * @param chunk 分块实体
     * @return 分块VO
     */
    private WikiChunkVO toChunkVO(WikiChunk chunk) {
        // 生成内容摘要
        String contentSummary = null;
        if (chunk.getContent() != null && chunk.getContent().length() > 100) {
            contentSummary = chunk.getContent().substring(0, 100) + "...";
        } else {
            contentSummary = chunk.getContent();
        }

        return WikiChunkVO.builder()
                .id(chunk.getId())
                .wikiId(chunk.getWikiId())
                .chunkIndex(chunk.getChunkIndex())
                .content(chunk.getContent())
                .contentSummary(contentSummary)
                .tokenCount(chunk.getTokenCount())
                .hasEmbedding(chunk.getEmbedding() != null && !chunk.getEmbedding().isEmpty())
                .createdAt(chunk.getCreatedAt())
                .updatedAt(chunk.getUpdatedAt())
                .build();
    }
}
