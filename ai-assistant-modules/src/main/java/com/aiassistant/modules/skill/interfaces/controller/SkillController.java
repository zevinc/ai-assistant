package com.aiassistant.modules.skill.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.skill.application.service.SkillApplicationService;
import com.aiassistant.modules.skill.domain.entity.Skill;
import com.aiassistant.modules.skill.interfaces.dto.SkillCreateRequest;
import com.aiassistant.modules.skill.interfaces.dto.SkillExecuteRequest;
import com.aiassistant.modules.skill.interfaces.vo.SkillVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 技能控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillApplicationService skillApplicationService;

    /**
     * 创建技能
     */
    @PostMapping
    public Result<SkillVO> createSkill(@RequestBody SkillCreateRequest request) {
        log.info("Creating skill: name={}", request.getName());

        Skill skill = Skill.builder()
                .name(request.getName())
                .description(request.getDescription())
                .skillType(request.getSkillType())
                .endpoint(request.getEndpoint())
                .inputSchema(request.getInputSchema())
                .outputSchema(request.getOutputSchema())
                .timeout(request.getTimeout() != null ? request.getTimeout() : 30000)
                .retryCount(request.getRetryCount() != null ? request.getRetryCount() : 0)
                .specId(request.getSpecId())
                .status(request.getStatus() != null ? request.getStatus() : 1)
                .build();

        Skill createdSkill = skillApplicationService.createSkill(skill);
        return Result.ok(SkillVO.fromEntity(createdSkill));
    }

    /**
     * 根据ID查询技能
     */
    @GetMapping("/{id}")
    public Result<SkillVO> getSkillById(@PathVariable Long id) {
        log.info("Getting skill by id: {}", id);
        return skillApplicationService.getSkillById(id)
                .map(skill -> Result.ok(SkillVO.fromEntity(skill)))
                .orElse(Result.fail(404, "Skill not found"));
    }

    /**
     * 查询所有技能
     */
    @GetMapping
    public Result<List<SkillVO>> getAllSkills() {
        log.info("Getting all skills");
        List<SkillVO> skills = skillApplicationService.getAllSkills().stream()
                .map(SkillVO::fromEntity)
                .collect(Collectors.toList());
        return Result.ok(skills);
    }

    /**
     * 根据规格ID查询技能
     */
    @GetMapping("/spec/{specId}")
    public Result<List<SkillVO>> getSkillsBySpecId(@PathVariable Long specId) {
        log.info("Getting skills by specId: {}", specId);
        List<SkillVO> skills = skillApplicationService.getSkillsBySpecId(specId).stream()
                .map(SkillVO::fromEntity)
                .collect(Collectors.toList());
        return Result.ok(skills);
    }

    /**
     * 根据类型查询技能
     */
    @GetMapping("/type/{skillType}")
    public Result<List<SkillVO>> getSkillsByType(@PathVariable String skillType) {
        log.info("Getting skills by type: {}", skillType);
        List<SkillVO> skills = skillApplicationService.getSkillsByType(skillType).stream()
                .map(SkillVO::fromEntity)
                .collect(Collectors.toList());
        return Result.ok(skills);
    }

    /**
     * 根据名称查询技能
     */
    @GetMapping("/name/{name}")
    public Result<SkillVO> getSkillByName(@PathVariable String name) {
        log.info("Getting skill by name: {}", name);
        return skillApplicationService.getSkillByName(name)
                .map(skill -> Result.ok(SkillVO.fromEntity(skill)))
                .orElse(Result.fail(404, "Skill not found"));
    }

    /**
     * 更新技能
     */
    @PutMapping("/{id}")
    public Result<SkillVO> updateSkill(@PathVariable Long id, @RequestBody SkillCreateRequest request) {
        log.info("Updating skill: id={}", id);

        Skill skill = Skill.builder()
                .name(request.getName())
                .description(request.getDescription())
                .skillType(request.getSkillType())
                .endpoint(request.getEndpoint())
                .inputSchema(request.getInputSchema())
                .outputSchema(request.getOutputSchema())
                .timeout(request.getTimeout())
                .retryCount(request.getRetryCount())
                .specId(request.getSpecId())
                .status(request.getStatus())
                .build();

        Skill updatedSkill = skillApplicationService.updateSkill(id, skill);
        return Result.ok(SkillVO.fromEntity(updatedSkill));
    }

    /**
     * 删除技能
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteSkill(@PathVariable Long id) {
        log.info("Deleting skill: id={}", id);
        skillApplicationService.deleteSkill(id);
        return Result.ok();
    }

    /**
     * 执行技能
     */
    @PostMapping("/execute")
    public Result<Map<String, Object>> executeSkill(@RequestBody SkillExecuteRequest request) {
        log.info("Executing skill: skillId={}", request.getSkillId());
        Map<String, Object> result = skillApplicationService.executeSkill(
                request.getSkillId(),
                request.getParams()
        );
        return Result.ok(result);
    }
}
