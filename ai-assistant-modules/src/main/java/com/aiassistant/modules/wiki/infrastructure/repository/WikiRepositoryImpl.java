package com.aiassistant.modules.wiki.infrastructure.repository;

import com.aiassistant.modules.wiki.domain.entity.Wiki;
import com.aiassistant.modules.wiki.domain.repository.WikiRepository;
import com.aiassistant.modules.wiki.infrastructure.mapper.WikiMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 知识库仓储实现
 * 实现知识库实体的持久化操作
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class WikiRepositoryImpl implements WikiRepository {

    private final WikiMapper wikiMapper;

    @Override
    public Optional<Wiki> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Wiki wiki = wikiMapper.selectById(id);
        return Optional.ofNullable(wiki);
    }

    @Override
    public List<Wiki> findBySpecId(Long specId) {
        if (specId == null) {
            return List.of();
        }

        LambdaQueryWrapper<Wiki> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Wiki::getSpecId, specId);
        queryWrapper.orderByDesc(Wiki::getCreatedAt);

        return wikiMapper.selectList(queryWrapper);
    }

    @Override
    public List<Wiki> findByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<Wiki> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Wiki::getCategory, category);
        queryWrapper.orderByDesc(Wiki::getCreatedAt);

        return wikiMapper.selectList(queryWrapper);
    }

    @Override
    public List<Wiki> searchByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<Wiki> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Wiki::getTitle, keyword);
        queryWrapper.orderByDesc(Wiki::getCreatedAt);

        return wikiMapper.selectList(queryWrapper);
    }

    @Override
    public List<Wiki> searchByContent(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<Wiki> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Wiki::getContent, keyword);
        queryWrapper.orderByDesc(Wiki::getCreatedAt);

        return wikiMapper.selectList(queryWrapper);
    }

    @Override
    public List<Wiki> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return listAll();
        }

        LambdaQueryWrapper<Wiki> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper
                .like(Wiki::getTitle, keyword)
                .or()
                .like(Wiki::getContent, keyword)
                .or()
                .like(Wiki::getTags, keyword)
        );
        queryWrapper.orderByDesc(Wiki::getCreatedAt);

        return wikiMapper.selectList(queryWrapper);
    }

    @Override
    public List<Wiki> listAll() {
        LambdaQueryWrapper<Wiki> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Wiki::getCreatedAt);

        return wikiMapper.selectList(queryWrapper);
    }

    @Override
    public Wiki save(Wiki wiki) {
        if (wiki == null) {
            throw new IllegalArgumentException("知识库不能为空");
        }

        int rows = wikiMapper.insert(wiki);
        if (rows <= 0) {
            throw new RuntimeException("保存知识库失败");
        }

        log.debug("保存知识库成功: ID={}", wiki.getId());
        return wiki;
    }

    @Override
    public Wiki update(Wiki wiki) {
        if (wiki == null || wiki.getId() == null) {
            throw new IllegalArgumentException("知识库或知识库ID不能为空");
        }

        int rows = wikiMapper.updateById(wiki);
        if (rows <= 0) {
            throw new RuntimeException("更新知识库失败: " + wiki.getId());
        }

        log.debug("更新知识库成功: ID={}", wiki.getId());
        return wiki;
    }

    @Override
    public boolean deleteById(Long id) {
        if (id == null) {
            return false;
        }

        int rows = wikiMapper.deleteById(id);
        boolean success = rows > 0;

        log.debug("删除知识库: ID={}, 结果={}", id, success);
        return success;
    }

    @Override
    public boolean existsByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }

        LambdaQueryWrapper<Wiki> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Wiki::getTitle, title);

        Long count = wikiMapper.selectCount(queryWrapper);
        return count != null && count > 0;
    }
}
