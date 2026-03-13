package com.aiassistant.modules.rule.domain.repository;

import com.aiassistant.modules.rule.domain.entity.Rule;

import java.util.List;
import java.util.Optional;

/**
 * 规则仓储接口
 * 定义规则实体的持久化操作
 */
public interface RuleRepository {

    /**
     * 根据ID查找规则
     *
     * @param id 规则ID
     * @return 规则实体
     */
    Optional<Rule> findById(Long id);

    /**
     * 根据规格ID查找规则列表
     *
     * @param specId 规格ID
     * @return 规则列表
     */
    List<Rule> findBySpecId(Long specId);

    /**
     * 根据类型查询规则列表
     *
     * @param ruleType 规则类型
     * @return 规则列表
     */
    List<Rule> listByType(String ruleType);

    /**
     * 查询所有规则
     *
     * @return 规则列表
     */
    List<Rule> listAll();

    /**
     * 根据规格ID和类型查询规则列表
     *
     * @param specId 规格ID
     * @param ruleType 规则类型
     * @return 规则列表
     */
    List<Rule> findBySpecIdAndType(Long specId, String ruleType);

    /**
     * 保存规则
     *
     * @param rule 规则实体
     * @return 保存后的规则
     */
    Rule save(Rule rule);

    /**
     * 更新规则
     *
     * @param rule 规则实体
     * @return 更新后的规则
     */
    Rule update(Rule rule);

    /**
     * 根据ID删除规则
     *
     * @param id 规则ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 根据规格ID删除所有规则
     *
     * @param specId 规格ID
     * @return 删除数量
     */
    int deleteBySpecId(Long specId);

    /**
     * 检查名称是否存在
     *
     * @param name 规则名称
     * @return 是否存在
     */
    boolean existsByName(String name);
}
