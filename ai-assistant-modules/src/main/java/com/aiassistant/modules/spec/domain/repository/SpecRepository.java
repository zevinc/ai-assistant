package com.aiassistant.modules.spec.domain.repository;

import com.aiassistant.modules.spec.domain.entity.Spec;

import java.util.List;
import java.util.Optional;

/**
 * 规格仓储接口
 * 定义规格实体的持久化操作
 */
public interface SpecRepository {

    /**
     * 根据ID查找规格
     *
     * @param id 规格ID
     * @return 规格实体
     */
    Optional<Spec> findById(Long id);

    /**
     * 根据名称查找规格
     *
     * @param name 规格名称
     * @return 规格实体
     */
    Optional<Spec> findByName(String name);

    /**
     * 根据状态查询规格列表
     *
     * @param status 状态
     * @return 规格列表
     */
    List<Spec> listByStatus(Integer status);

    /**
     * 查询所有规格
     *
     * @return 规格列表
     */
    List<Spec> listAll();

    /**
     * 保存规格
     *
     * @param spec 规格实体
     * @return 保存后的规格
     */
    Spec save(Spec spec);

    /**
     * 更新规格
     *
     * @param spec 规格实体
     * @return 更新后的规格
     */
    Spec update(Spec spec);

    /**
     * 根据ID删除规格
     *
     * @param id 规格ID
     * @return 是否删除成功
     */
    boolean deleteById(Long id);

    /**
     * 检查名称是否存在
     *
     * @param name 规格名称
     * @return 是否存在
     */
    boolean existsByName(String name);

    /**
     * 检查名称是否存在（排除指定ID）
     *
     * @param name 规格名称
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsByNameAndIdNot(String name, Long excludeId);
}
