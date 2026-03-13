package com.aiassistant.modules.wiki.infrastructure.repository;

import com.aiassistant.modules.wiki.domain.entity.WikiChunk;
import com.aiassistant.modules.wiki.domain.repository.WikiChunkRepository;
import com.aiassistant.modules.wiki.infrastructure.mapper.WikiChunkMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 知识库分块仓储实现
 * 实现知识库分块实体的持久化操作
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class WikiChunkRepositoryImpl implements WikiChunkRepository {

    private final WikiChunkMapper wikiChunkMapper;

    @Override
    public List<WikiChunk> findByWikiId(Long wikiId) {
        if (wikiId == null) {
            return List.of();
        }

        LambdaQueryWrapper<WikiChunk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WikiChunk::getWikiId, wikiId);
        queryWrapper.orderByAsc(WikiChunk::getChunkIndex);

        return wikiChunkMapper.selectList(queryWrapper);
    }

    @Override
    public List<WikiChunk> findByWikiIdAndIndexRange(Long wikiId, int startIndex, int endIndex) {
        if (wikiId == null) {
            return List.of();
        }

        LambdaQueryWrapper<WikiChunk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WikiChunk::getWikiId, wikiId);
        queryWrapper.ge(WikiChunk::getChunkIndex, startIndex);
        queryWrapper.le(WikiChunk::getChunkIndex, endIndex);
        queryWrapper.orderByAsc(WikiChunk::getChunkIndex);

        return wikiChunkMapper.selectList(queryWrapper);
    }

    @Override
    public WikiChunk findById(Long id) {
        if (id == null) {
            return null;
        }
        return wikiChunkMapper.selectById(id);
    }

    @Override
    public WikiChunk save(WikiChunk chunk) {
        if (chunk == null) {
            throw new IllegalArgumentException("分块不能为空");
        }

        int rows = wikiChunkMapper.insert(chunk);
        if (rows <= 0) {
            throw new RuntimeException("保存分块失败");
        }

        log.debug("保存分块成功: ID={}", chunk.getId());
        return chunk;
    }

    @Override
    public int saveChunks(List<WikiChunk> chunks) {
        if (chunks == null || chunks.isEmpty()) {
            return 0;
        }

        int savedCount = 0;
        for (WikiChunk chunk : chunks) {
            try {
                wikiChunkMapper.insert(chunk);
                savedCount++;
            } catch (Exception e) {
                log.error("保存分块失败: wikiId={}, chunkIndex={}",
                        chunk.getWikiId(), chunk.getChunkIndex(), e);
            }
        }

        log.debug("批量保存分块完成: 总数={}, 成功={}", chunks.size(), savedCount);
        return savedCount;
    }

    @Override
    public WikiChunk update(WikiChunk chunk) {
        if (chunk == null || chunk.getId() == null) {
            throw new IllegalArgumentException("分块或分块ID不能为空");
        }

        int rows = wikiChunkMapper.updateById(chunk);
        if (rows <= 0) {
            throw new RuntimeException("更新分块失败: " + chunk.getId());
        }

        log.debug("更新分块成功: ID={}", chunk.getId());
        return chunk;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }

        int rows = wikiChunkMapper.deleteById(id);
        boolean success = rows > 0;

        log.debug("删除分块: ID={}, 结果={}", id, success);
        return success;
    }

    @Override
    public int deleteByWikiId(Long wikiId) {
        if (wikiId == null) {
            return 0;
        }

        LambdaQueryWrapper<WikiChunk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WikiChunk::getWikiId, wikiId);

        int rows = wikiChunkMapper.delete(queryWrapper);
        log.debug("根据知识库ID删除分块: wikiId={}, 删除数量={}", wikiId, rows);
        return rows;
    }

    @Override
    public long countByWikiId(Long wikiId) {
        if (wikiId == null) {
            return 0;
        }

        LambdaQueryWrapper<WikiChunk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WikiChunk::getWikiId, wikiId);

        Long count = wikiChunkMapper.selectCount(queryWrapper);
        return count != null ? count : 0;
    }

    @Override
    public List<WikiChunk> searchByContent(String keyword, int limit) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<WikiChunk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(WikiChunk::getContent, keyword);
        queryWrapper.last("LIMIT " + limit);

        return wikiChunkMapper.selectList(queryWrapper);
    }
}
