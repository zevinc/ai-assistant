package com.aiassistant.modules.spec.application.service;

import com.aiassistant.common.event.DomainEventPublisher;
import com.aiassistant.common.event.SimpleDomainEvent;
import com.aiassistant.modules.spec.domain.entity.Spec;
import com.aiassistant.modules.spec.domain.enums.SpecStatusEnum;
import com.aiassistant.modules.spec.domain.repository.SpecRepository;
import com.aiassistant.modules.spec.domain.service.SpecDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 规格应用服务
 * 提供规格的CRUD操作和业务流程编排
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpecApplicationService {

    private final SpecRepository specRepository;
    private final SpecDomainService specDomainService;
    private final DomainEventPublisher domainEventPublisher;

    /**
     * 创建规格
     *
     * @param spec 规格实体
     * @return 创建后的规格
     */
    @Transactional(rollbackFor = Exception.class)
    public Spec create(Spec spec) {
        // 设置默认状态为草稿
        if (spec.getStatus() == null) {
            spec.setStatus(SpecStatusEnum.DRAFT.getCode());
        }

        // 验证规格
        specDomainService.validateSpec(spec);

        // 检查名称是否已存在
        if (specRepository.existsByName(spec.getName())) {
            throw new IllegalArgumentException("规格名称已存在: " + spec.getName());
        }

        // 保存规格
        Spec savedSpec = specRepository.save(spec);

        // 发布领域事件
        publishEvent(new SimpleDomainEvent(String.valueOf(savedSpec.getId()), "Spec", "spec.created"));

        log.info("创建规格成功: {} (ID: {})", savedSpec.getName(), savedSpec.getId());
        return savedSpec;
    }

    /**
     * 更新规格
     *
     * @param spec 规格实体
     * @return 更新后的规格
     */
    @Transactional(rollbackFor = Exception.class)
    public Spec update(Spec spec) {
        // 检查规格是否存在
        Spec existingSpec = specRepository.findById(spec.getId())
                .orElseThrow(() -> new IllegalArgumentException("规格不存在: " + spec.getId()));

        // 检查是否可以修改
        if (!specDomainService.canModify(spec.getId())) {
            throw new IllegalArgumentException("当前状态的规格不允许修改");
        }

        // 检查名称是否与其他规格冲突
        if (!existingSpec.getName().equals(spec.getName()) &&
                specRepository.existsByNameAndIdNot(spec.getName(), spec.getId())) {
            throw new IllegalArgumentException("规格名称已存在: " + spec.getName());
        }

        // 验证规格
        specDomainService.validateSpec(spec);

        // 保留不可修改的字段
        spec.setStatus(existingSpec.getStatus());
        spec.setCreatedAt(existingSpec.getCreatedAt());
        spec.setCreatedBy(existingSpec.getCreatedBy());

        // 更新规格
        Spec updatedSpec = specRepository.update(spec);

        // 发布领域事件
        publishEvent(new SimpleDomainEvent(String.valueOf(updatedSpec.getId()), "Spec", "spec.updated"));

        log.info("更新规格成功: {} (ID: {})", updatedSpec.getName(), updatedSpec.getId());
        return updatedSpec;
    }

    /**
     * 根据ID查询规格
     *
     * @param id 规格ID
     * @return 规格实体
     */
    public Optional<Spec> findById(Long id) {
        return specRepository.findById(id);
    }

    /**
     * 根据名称查询规格
     *
     * @param name 规格名称
     * @return 规格实体
     */
    public Optional<Spec> findByName(String name) {
        return specRepository.findByName(name);
    }

    /**
     * 查询所有规格
     *
     * @return 规格列表
     */
    public List<Spec> listAll() {
        return specRepository.listAll();
    }

    /**
     * 根据状态查询规格列表
     *
     * @param status 状态
     * @return 规格列表
     */
    public List<Spec> listByStatus(Integer status) {
        return specRepository.listByStatus(status);
    }

    /**
     * 删除规格
     *
     * @param id 规格ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        // 检查规格是否存在
        Spec spec = specRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("规格不存在: " + id));

        // 只有草稿状态可以删除
        if (!SpecStatusEnum.DRAFT.getCode().equals(spec.getStatus())) {
            throw new IllegalArgumentException("只有草稿状态的规格才能删除");
        }

        // 删除规格
        specRepository.deleteById(id);

        // 发布领域事件
        publishEvent(new SimpleDomainEvent(String.valueOf(id), "Spec", "spec.deleted"));

        log.info("删除规格成功: {} (ID: {})", spec.getName(), id);
    }

    /**
     * 发布规格
     *
     * @param specId 规格ID
     * @return 发布后的规格
     */
    @Transactional(rollbackFor = Exception.class)
    public Spec publish(Long specId) {
        Spec spec = specDomainService.publishSpec(specId);
        Spec updatedSpec = specRepository.update(spec);

        // 发布领域事件
        publishEvent(new SimpleDomainEvent(String.valueOf(specId), "Spec", "spec.published"));

        log.info("发布规格成功: {} (ID: {})", spec.getName(), specId);
        return updatedSpec;
    }

    /**
     * 归档规格
     *
     * @param specId 规格ID
     * @return 归档后的规格
     */
    @Transactional(rollbackFor = Exception.class)
    public Spec archive(Long specId) {
        Spec spec = specDomainService.archiveSpec(specId);
        Spec updatedSpec = specRepository.update(spec);

        // 发布领域事件
        publishEvent(new SimpleDomainEvent(String.valueOf(specId), "Spec", "spec.archived"));

        log.info("归档规格成功: {} (ID: {})", spec.getName(), specId);
        return updatedSpec;
    }

    /**
     * 发布领域事件
     *
     * @param event 领域事件
     */
    private void publishEvent(SimpleDomainEvent event) {
        try {
            domainEventPublisher.publish(event);
        } catch (Exception e) {
            log.warn("发布领域事件失败: {}", event, e);
        }
    }
}
