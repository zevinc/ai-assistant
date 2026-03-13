package com.aiassistant.modules.prompt.domain.repository;

import com.aiassistant.modules.prompt.domain.entity.PromptTemplate;

import java.util.List;
import java.util.Optional;

/**
 * Prompt模板仓储接口
 */
public interface PromptTemplateRepository {

    /**
     * 根据ID查找模板
     *
     * @param id 模板ID
     * @return 模板实体
     */
    Optional<PromptTemplate> findById(Long id);

    /**
     * 根据名称查找模板
     *
     * @param name 模板名称
     * @return 模板实体
     */
    Optional<PromptTemplate> findByName(String name);

    /**
     * 根据分类查找模板列表
     *
     * @param category 分类
     * @return 模板列表
     */
    List<PromptTemplate> findByCategory(String category);

    /**
     * 根据规格ID查找模板列表
     *
     * @param specId 规格ID
     * @return 模板列表
     */
    List<PromptTemplate> findBySpecId(Long specId);

    /**
     * 查找模板的最新版本
     *
     * @param name 模板名称
     * @return 模板实体
     */
    Optional<PromptTemplate> findLatestVersion(String name);

    /**
     * 保存模板
     *
     * @param template 模板实体
     * @return 保存后的模板
     */
    PromptTemplate save(PromptTemplate template);

    /**
     * 更新模板
     *
     * @param template 模板实体
     * @return 更新后的模板
     */
    PromptTemplate update(PromptTemplate template);

    /**
     * 根据ID删除模板
     *
     * @param id 模板ID
     */
    void deleteById(Long id);
}
