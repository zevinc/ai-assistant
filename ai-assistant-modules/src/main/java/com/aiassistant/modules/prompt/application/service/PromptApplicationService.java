package com.aiassistant.modules.prompt.application.service;

import com.aiassistant.common.event.DomainEventPublisher;
import com.aiassistant.common.event.SimpleDomainEvent;
import com.aiassistant.modules.prompt.domain.entity.PromptTemplate;
import com.aiassistant.modules.prompt.domain.repository.PromptTemplateRepository;
import com.aiassistant.modules.prompt.domain.service.PromptRenderer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Prompt应用服务
 * 提供Prompt模板管理的业务用例
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PromptApplicationService {

    private final PromptTemplateRepository promptTemplateRepository;
    private final PromptRenderer promptRenderer;
    private final DomainEventPublisher eventPublisher;

    /**
     * 创建Prompt模板
     *
     * @param name        模板名称
     * @param description 模板描述
     * @param category    分类
     * @param template    模板内容
     * @param variables   变量列表
     * @param specId      规格ID
     * @return 创建的模板
     */
    @Transactional
    public PromptTemplate createPromptTemplate(String name, String description, String category,
                                               String template, String variables, Long specId) {
        log.info("Creating prompt template: {}", name);

        // 查找最新版本号
        Integer latestVersion = promptTemplateRepository.findLatestVersion(name)
                .map(t -> t.getVersion() + 1)
                .orElse(1);

        PromptTemplate promptTemplate = PromptTemplate.builder()
                .name(name)
                .description(description)
                .category(category)
                .template(template)
                .variables(variables)
                .version(latestVersion)
                .specId(specId)
                .status(0) // 草稿状态
                .build();

        PromptTemplate savedTemplate = promptTemplateRepository.save(promptTemplate);

        // 发布模板创建事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(savedTemplate.getId()), "PromptTemplate", "prompt.template.created"));

        log.info("Prompt template created successfully with id: {}, version: {}",
                savedTemplate.getId(), savedTemplate.getVersion());
        return savedTemplate;
    }

    /**
     * 根据ID获取模板
     *
     * @param id 模板ID
     * @return 模板实体
     */
    public PromptTemplate getPromptTemplate(Long id) {
        return promptTemplateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prompt template not found: " + id));
    }

    /**
     * 根据名称获取模板
     *
     * @param name 模板名称
     * @return 模板实体
     */
    public PromptTemplate getPromptTemplateByName(String name) {
        return promptTemplateRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Prompt template not found: " + name));
    }

    /**
     * 根据分类获取模板列表
     *
     * @param category 分类
     * @return 模板列表
     */
    public List<PromptTemplate> getPromptTemplatesByCategory(String category) {
        return promptTemplateRepository.findByCategory(category);
    }

    /**
     * 根据规格ID获取模板列表
     *
     * @param specId 规格ID
     * @return 模板列表
     */
    public List<PromptTemplate> getPromptTemplatesBySpecId(Long specId) {
        return promptTemplateRepository.findBySpecId(specId);
    }

    /**
     * 更新模板
     *
     * @param id          模板ID
     * @param description 模板描述
     * @param template    模板内容
     * @param variables   变量列表
     * @return 更新后的模板
     */
    @Transactional
    public PromptTemplate updatePromptTemplate(Long id, String description,
                                               String template, String variables) {
        log.info("Updating prompt template: {}", id);

        PromptTemplate promptTemplate = promptTemplateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prompt template not found: " + id));

        if (description != null) {
            promptTemplate.setDescription(description);
        }
        if (template != null) {
            promptTemplate.setTemplate(template);
        }
        if (variables != null) {
            promptTemplate.setVariables(variables);
        }

        PromptTemplate updatedTemplate = promptTemplateRepository.update(promptTemplate);

        // 发布模板更新事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(id), "PromptTemplate", "prompt.template.updated"));

        log.info("Prompt template updated successfully: {}", id);
        return updatedTemplate;
    }

    /**
     * 删除模板
     *
     * @param id 模板ID
     */
    @Transactional
    public void deletePromptTemplate(Long id) {
        log.info("Deleting prompt template: {}", id);

        promptTemplateRepository.deleteById(id);

        // 发布模板删除事件
        eventPublisher.publish(new SimpleDomainEvent(String.valueOf(id), "PromptTemplate", "prompt.template.deleted"));

        log.info("Prompt template deleted successfully: {}", id);
    }

    /**
     * 渲染Prompt模板
     *
     * @param templateId 模板ID
     * @param variables  变量映射
     * @return 渲染后的字符串
     */
    public String renderPrompt(Long templateId, Map<String, Object> variables) {
        log.info("Rendering prompt template: {}", templateId);

        PromptTemplate template = promptTemplateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Prompt template not found: " + templateId));

        String renderedPrompt = promptRenderer.render(template, variables);

        log.debug("Prompt rendered successfully for template: {}", templateId);
        return renderedPrompt;
    }

    /**
     * 验证模板变量
     *
     * @param templateId 模板ID
     * @param variables  变量映射
     * @return 是否完整
     */
    public boolean validateVariables(Long templateId, Map<String, Object> variables) {
        PromptTemplate template = promptTemplateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Prompt template not found: " + templateId));

        return promptRenderer.validateVariables(template, variables);
    }

    /**
     * 激活模板
     *
     * @param id 模板ID
     * @return 更新后的模板
     */
    @Transactional
    public PromptTemplate activateTemplate(Long id) {
        log.info("Activating prompt template: {}", id);

        PromptTemplate template = promptTemplateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prompt template not found: " + id));

        template.setStatus(1); // 激活状态
        PromptTemplate updatedTemplate = promptTemplateRepository.update(template);

        log.info("Prompt template activated successfully: {}", id);
        return updatedTemplate;
    }
}
