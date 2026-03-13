package com.aiassistant.modules.skill.infrastructure.mapper;

import com.aiassistant.modules.skill.domain.entity.Skill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 技能Mapper接口
 */
@Mapper
public interface SkillMapper extends BaseMapper<Skill> {
}
