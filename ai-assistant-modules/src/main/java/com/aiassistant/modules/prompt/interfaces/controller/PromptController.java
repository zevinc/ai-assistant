package com.aiassistant.modules.prompt.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.prompt.application.service.PromptApplicationService;
import com.aiassistant.modules.prompt.domain.entity.PromptTemplate;
import com.aiassistant.modules.prompt.interfaces.dto.PromptCreateRequest;
import com.aiassistant.modules.prompt.interfaces.dto.PromptRenderRequest;
import com.aiassistant.modules.prompt.interfaces.vo.PromptTemplateVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Prompt控制器
 * 提供Prompt模板管理的REST API
 */
@RestController
@RequestMapping("/api/v1/prompts")
@Slf4j
@RequiredArgsConstructor
public class PromptController {

    private final PromptApplicationService promptApplicationService;

    /**
     * 创建Prompt模板
     *
     * @param request 模板创建请求
     * @return 创建的模板
     */
    @PostMapping
    public Result<PromptTemplateVO> create(@RequestBody PromptCreateRequest request) {
        log.info("Creating prompt template: {}", request.getName());

        PromptTemplate template = promptApplicationService.createPromptTemplate(
                request.getName(),
                request.getDescription(),
                request.getCategory(),
                request.getTemplate(),
                request.getVariables(),
                request.getSpecId()
        );

        return Result.ok(toVO(template));
    }

    /**
     * 获取模板详情
     *
     * @param id 模板ID
     * @return 模板详情
     */
    @GetMapping("/{id}")
    public Result<PromptTemplateVO> get(@PathVariable Long id) {
        log.info("Getting prompt template: {}", id);
        PromptTemplate template = promptApplicationService.getPromptTemplate(id);
        return Result.ok(toVO(template));
    }

    /**
     * 根据名称获取模板
     *
     * @param name 模板名称
     * @return 模板详情
     */
    @GetMapping("/name/{name}")
    public Result<PromptTemplateVO> getByName(@PathVariable String name) {
        log.info("Getting prompt template by name: {}", name);
        PromptTemplate template = promptApplicationService.getPromptTemplateByName(name);
        return Result.ok(toVO(template));
    }

    /**
     * 根据分类获取模板列表
     *
     * @param category 分类
     * @return 模板列表
     */
    @GetMapping("/category/{category}")
    public Result<List<PromptTemplateVO>> getByCategory(@PathVariable String category) {
        log.info("Getting prompt templates by category: {}", category);
        List<PromptTemplate> templates = promptApplicationService.getPromptTemplatesByCategory(category);
        List<PromptTemplateVO> voList = templates.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(voList);
    }

    /**
     * 根据规格ID获取模板列表
     *
     * @param specId 规格ID
     * @return 模板列表
     */
    @GetMapping("/spec/{specId}")
    public Result<List<PromptTemplateVO>> getBySpecId(@PathVariable Long specId) {
        log.info("Getting prompt templates by spec: {}", specId);
        List<PromptTemplate> templates = promptApplicationService.getPromptTemplatesBySpecId(specId);
        List<PromptTemplateVO> voList = templates.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return Result.ok(voList);
    }

    /**
     * 更新模板
     *
     * @param id      模板ID
     * @param request 模板更新请求
     * @return 更新后的模板
     */
    @PutMapping("/{id}")
    public Result<PromptTemplateVO> update(@PathVariable Long id, @RequestBody PromptCreateRequest request) {
        log.info("Updating prompt template: {}", id);

        PromptTemplate template = promptApplicationService.updatePromptTemplate(
                id,
                request.getDescription(),
                request.getTemplate(),
                request.getVariables()
        );

        return Result.ok(toVO(template));
    }

    /**
     * 删除模板
     *
     * @param id 模板ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        log.info("Deleting prompt template: {}", id);
        promptApplicationService.deletePromptTemplate(id);
        return Result.ok();
    }

    /**
     * 渲染Prompt模板
     *
     * @param request 渲染请求
     * @return 渲染结果
     */
    @PostMapping("/render")
    public Result<String> render(@RequestBody PromptRenderRequest request) {
        log.info("Rendering prompt template: {}", request.getTemplateId());

        String renderedPrompt = promptApplicationService.renderPrompt(
                request.getTemplateId(),
                request.getVariables()
        );

        return Result.ok(renderedPrompt);
    }

    /**
     * 验证模板变量
     *
     * @param request 渲染请求
     * @return 验证结果
     */
    @PostMapping("/validate")
    public Result<Boolean> validate(@RequestBody PromptRenderRequest request) {
        log.info("Validating prompt template variables: {}", request.getTemplateId());

        boolean isValid = promptApplicationService.validateVariables(
                request.getTemplateId(),
                request.getVariables()
        );

        return Result.ok(isValid);
    }

    /**
     * 激活模板
     *
     * @param id 模板ID
     * @return 更新后的模板
     */
    @PostMapping("/{id}/activate")
    public Result<PromptTemplateVO> activate(@PathVariable Long id) {
        log.info("Activating prompt template: {}", id);
        PromptTemplate template = promptApplicationService.activateTemplate(id);
        return Result.ok(toVO(template));
    }

    /**
     * 将实体转换为VO
     */
    private PromptTemplateVO toVO(PromptTemplate template) {
        return PromptTemplateVO.builder()
                .id(template.getId())
                .name(template.getName())
                .description(template.getDescription())
                .category(template.getCategory())
                .template(template.getTemplate())
                .variables(template.getVariables())
                .version(template.getVersion())
                .specId(template.getSpecId())
                .status(template.getStatus())
                .createdAt(template.getCreatedAt())
                .updatedAt(template.getUpdatedAt())
                .build();
    }
}
