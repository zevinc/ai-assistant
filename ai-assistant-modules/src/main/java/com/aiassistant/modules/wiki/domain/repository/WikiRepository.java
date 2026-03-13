package com.aiassistant.modules.wiki.domain.repository;

import com.aiassistant.modules.wiki.domain.entity.Wiki;

import java.util.List;
import java.util.Optional;

/**
 * 知识库仓储接口
 * 定义知识库实体的持久化操作
 */
public interface WikiRepository {

    /**
     * 根据ID查找知识库
     *
     * @param id 知识库ID
     * @return 知识库实体
     */
    Optional<Wiki> findById(Long id);

    /**
     * 根据规格ID查找知识库列表
     *
     * @param specId 规格ID
     * @return 知识库列表
     */
    List<Wiki> findBySpecId(Long specId);

    /**
     * 根据分类查询知识库列表
     *
     * @param category 分类
     * @return 知识库列表
     */
    List<Wiki> findByCategory(String category);

    /**
     * 根据标题模糊查询
     *
     * @param keyword 关键词
     * @return 知识库列表
     */
    List<Wiki> searchByTitle(String keyword);

    /**
     * 根据内容模糊查询
     *
     * @param keyword 关键词
     * @return 知识库列表
     */
    List<Wiki> searchByContent(String keyword);

    /**
     * 综合搜索（标题+内容）
     *
     * @param keyword 关键词
     * @return 知识库列表
     */
    List<Wiki> search(String keyword);

    /**
     * 查询所有知识库
     *
     * @return 知识库列表
     */
    List<Wiki> listAll();

    /**
     * 保存知识库
     *
     * @param wiki 知识库实体
     * @return 保存后的知识库
     */
    Wiki save(Wiki wiki);

    /**
     * 更新知识库
     *
     * @param wiki 知识库实体
     * @return 更新后的知识库
     */
    Wiki update(Wiki wiki);

    /**
     * 根据ID删除知识库
     *
     * @param id 知识库ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 检查标题是否存在
     *
     * @param title 标题
     * @return 是否存在
     */
    boolean existsByTitle(String title);
}
