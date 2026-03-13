package com.aiassistant.modules.skill.domain.repository;

import com.aiassistant.modules.skill.domain.entity.Skill;

import java.util.List;
import java.util.Optional;

/**
 * 技能仓储接口
 */
public interface SkillRepository {

    /**
     * 根据ID查询技能
     */
    Optional<Skill> findById(Long id);

    /**
     * 根据规格ID查询技能列表
     */
    List<Skill> findBySpecId(Long specId);

    /**
     * 根据类型查询技能列表
     */
    List<Skill> findByType(String skillType);

    /**
     * 根据名称查询技能
     */
    Optional<Skill> findByName(String name);

    /**
     * 查询所有技能
     */
    List<Skill> findAll();

    /**
     * 保存技能
     */
    Skill save(Skill skill);

    /**
     * 更新技能
     */
    Skill update(Skill skill);

    /**
     * 根据ID删除技能
     */
    void deleteById(Long id);
}
