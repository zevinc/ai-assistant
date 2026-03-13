package com.aiassistant.modules.skill.infrastructure.repository;

import com.aiassistant.modules.skill.domain.entity.Skill;
import com.aiassistant.modules.skill.domain.repository.SkillRepository;
import com.aiassistant.modules.skill.infrastructure.mapper.SkillMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 技能仓储实现
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SkillRepositoryImpl implements SkillRepository {

    private final SkillMapper skillMapper;

    @Override
    public Optional<Skill> findById(Long id) {
        return Optional.ofNullable(skillMapper.selectById(id));
    }

    @Override
    public List<Skill> findBySpecId(Long specId) {
        LambdaQueryWrapper<Skill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Skill::getSpecId, specId);
        return skillMapper.selectList(wrapper);
    }

    @Override
    public List<Skill> findByType(String skillType) {
        LambdaQueryWrapper<Skill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Skill::getSkillType, skillType);
        return skillMapper.selectList(wrapper);
    }

    @Override
    public Optional<Skill> findByName(String name) {
        LambdaQueryWrapper<Skill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Skill::getName, name);
        return Optional.ofNullable(skillMapper.selectOne(wrapper));
    }

    @Override
    public List<Skill> findAll() {
        return skillMapper.selectList(null);
    }

    @Override
    public Skill save(Skill skill) {
        skillMapper.insert(skill);
        return skill;
    }

    @Override
    public Skill update(Skill skill) {
        skillMapper.updateById(skill);
        return skill;
    }

    @Override
    public void deleteById(Long id) {
        skillMapper.deleteById(id);
    }
}
