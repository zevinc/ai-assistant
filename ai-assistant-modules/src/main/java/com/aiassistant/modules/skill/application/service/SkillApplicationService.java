package com.aiassistant.modules.skill.application.service;

import com.aiassistant.common.event.DomainEventPublisher;
import com.aiassistant.common.event.SimpleDomainEvent;
import com.aiassistant.modules.skill.domain.entity.Skill;
import com.aiassistant.modules.skill.domain.repository.SkillRepository;
import com.aiassistant.modules.skill.domain.service.SkillExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 技能应用服务
 * 提供技能的CRUD和执行功能
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SkillApplicationService {

    private final SkillRepository skillRepository;
    private final SkillExecutor skillExecutor;
    private final DomainEventPublisher domainEventPublisher;

    /**
     * 创建技能
     */
    @Transactional
    public Skill createSkill(Skill skill) {
        log.info("Creating skill: name={}, type={}", skill.getName(), skill.getSkillType());

        // 检查名称是否已存在
        Optional<Skill> existingSkill = skillRepository.findByName(skill.getName());
        if (existingSkill.isPresent()) {
            throw new IllegalArgumentException("Skill with name '" + skill.getName() + "' already exists");
        }

        Skill savedSkill = skillRepository.save(skill);

        // 发布领域事件
        publishEvent("skill.created", savedSkill);

        return savedSkill;
    }

    /**
     * 根据ID查询技能
     */
    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }

    /**
     * 根据名称查询技能
     */
    public Optional<Skill> getSkillByName(String name) {
        return skillRepository.findByName(name);
    }

    /**
     * 查询所有技能
     */
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    /**
     * 根据规格ID查询技能
     */
    public List<Skill> getSkillsBySpecId(Long specId) {
        return skillRepository.findBySpecId(specId);
    }

    /**
     * 根据类型查询技能
     */
    public List<Skill> getSkillsByType(String skillType) {
        return skillRepository.findByType(skillType);
    }

    /**
     * 更新技能
     */
    @Transactional
    public Skill updateSkill(Long id, Skill skill) {
        log.info("Updating skill: id={}", id);

        Skill existingSkill = skillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + id));

        // 更新字段
        if (skill.getName() != null) {
            existingSkill.setName(skill.getName());
        }
        if (skill.getDescription() != null) {
            existingSkill.setDescription(skill.getDescription());
        }
        if (skill.getSkillType() != null) {
            existingSkill.setSkillType(skill.getSkillType());
        }
        if (skill.getEndpoint() != null) {
            existingSkill.setEndpoint(skill.getEndpoint());
        }
        if (skill.getInputSchema() != null) {
            existingSkill.setInputSchema(skill.getInputSchema());
        }
        if (skill.getOutputSchema() != null) {
            existingSkill.setOutputSchema(skill.getOutputSchema());
        }
        if (skill.getTimeout() != null) {
            existingSkill.setTimeout(skill.getTimeout());
        }
        if (skill.getRetryCount() != null) {
            existingSkill.setRetryCount(skill.getRetryCount());
        }
        if (skill.getSpecId() != null) {
            existingSkill.setSpecId(skill.getSpecId());
        }
        if (skill.getStatus() != null) {
            existingSkill.setStatus(skill.getStatus());
        }

        Skill updatedSkill = skillRepository.update(existingSkill);

        // 发布领域事件
        publishEvent("skill.updated", updatedSkill);

        return updatedSkill;
    }

    /**
     * 删除技能
     */
    @Transactional
    public void deleteSkill(Long id) {
        log.info("Deleting skill: id={}", id);

        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + id));

        skillRepository.deleteById(id);

        // 发布领域事件
        publishEvent("skill.deleted", skill);
    }

    /**
     * 执行技能
     */
    public Map<String, Object> executeSkill(Long skillId, Map<String, Object> params) {
        log.info("Executing skill: skillId={}", skillId);

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found with id: " + skillId));

        // 检查技能状态
        if (skill.getStatus() == null || skill.getStatus() != 1) {
            throw new IllegalStateException("Skill is not enabled: " + skillId);
        }

        Map<String, Object> result = skillExecutor.execute(skill, params);

        // 发布领域事件
        publishEvent("skill.executed", skill);

        return result;
    }

    /**
     * 发布领域事件
     */
    private void publishEvent(String eventType, Skill skill) {
        try {
            domainEventPublisher.publish(new SimpleDomainEvent(String.valueOf(skill.getId()), "Skill", eventType));
        } catch (Exception e) {
            log.warn("Failed to publish domain event: {}", e.getMessage());
        }
    }
}
