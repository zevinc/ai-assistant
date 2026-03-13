package com.aiassistant.modules.prompt.infrastructure.repository;

import com.aiassistant.modules.prompt.domain.entity.PromptTemplate;
import com.aiassistant.modules.prompt.domain.repository.PromptTemplateRepository;
import com.aiassistant.modules.prompt.infrastructure.mapper.PromptTemplateMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Prompt模板仓储实现
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class PromptTemplateRepositoryImpl implements PromptTemplateRepository {

    private final PromptTemplateMapper promptTemplateMapper;

    @Override
    public Optional<PromptTemplate> findById(Long id) {
        return Optional.ofNullable(promptTemplateMapper.selectById(id));
    }

    @Override
    public Optional<PromptTemplate> findByName(String name) {
        LambdaQueryWrapper<PromptTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PromptTemplate::getName, name)
                .orderByDesc(PromptTemplate::getVersion)
                .last("LIMIT 1");
        return Optional.ofNullable(promptTemplateMapper.selectOne(queryWrapper));
    }

    @Override
    public List<PromptTemplate> findByCategory(String category) {
        LambdaQueryWrapper<PromptTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PromptTemplate::getCategory, category)
                .orderByDesc(PromptTemplate::getCreatedAt);
        return promptTemplateMapper.selectList(queryWrapper);
    }

    @Override
    public List<PromptTemplate> findBySpecId(Long specId) {
        LambdaQueryWrapper<PromptTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PromptTemplate::getSpecId, specId)
                .orderByDesc(PromptTemplate::getCreatedAt);
        return promptTemplateMapper.selectList(queryWrapper);
    }

    @Override
    public Optional<PromptTemplate> findLatestVersion(String name) {
        LambdaQueryWrapper<PromptTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PromptTemplate::getName, name)
                .orderByDesc(PromptTemplate::getVersion)
                .last("LIMIT 1");
        return Optional.ofNullable(promptTemplateMapper.selectOne(queryWrapper));
    }

    @Override
    public PromptTemplate save(PromptTemplate template) {
        promptTemplateMapper.insert(template);
        return template;
    }

    @Override
    public PromptTemplate update(PromptTemplate template) {
        promptTemplateMapper.updateById(template);
        return template;
    }

    @Override
    public void deleteById(Long id) {
        promptTemplateMapper.deleteById(id);
    }
}
