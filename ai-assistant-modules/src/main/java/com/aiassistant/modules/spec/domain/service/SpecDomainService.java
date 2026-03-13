package com.aiassistant.modules.spec.domain.service;

import com.aiassistant.modules.spec.domain.entity.Spec;
import com.aiassistant.modules.spec.domain.enums.SpecStatusEnum;
import com.aiassistant.modules.spec.domain.repository.SpecRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 规格领域服务
 * 处理规格相关的业务规则和领域逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpecDomainService {

    private final SpecRepository specRepository;

    /**
     * 验证规格
     *
     * @param spec 规格实体
     * @throws IllegalArgumentException 验证失败时抛出
     */
    public void validateSpec(Spec spec) {
        if (spec == null) {
            throw new IllegalArgumentException("规格不能为空");
        }

        if (spec.getName() == null || spec.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("规格名称不能为空");
        }

        if (spec.getTemperature() != null) {
            if (spec.getTemperature() < 0.0 || spec.getTemperature() > 2.0) {
                throw new IllegalArgumentException("温度参数必须在0.0到2.0之间");
            }
        }

        if (spec.getMaxTokens() != null && spec.getMaxTokens() <= 0) {
            throw new IllegalArgumentException("最大令牌数必须大于0");
        }

        if (spec.getModelId() == null || spec.getModelId().trim().isEmpty()) {
            throw new IllegalArgumentException("模型ID不能为空");
        }

        // 验证状态
        if (spec.getStatus() != null && !SpecStatusEnum.isValidCode(spec.getStatus())) {
            throw new IllegalArgumentException("无效的状态值");
        }

        log.debug("规格验证通过: {}", spec.getName());
    }

    /**
     * 发布规格
     *
     * @param specId 规格ID
     * @return 更新后的规格
     * @throws IllegalArgumentException 规格不存在或状态不允许发布
     */
    public Spec publishSpec(Long specId) {
        Spec spec = specRepository.findById(specId)
                .orElseThrow(() -> new IllegalArgumentException("规格不存在: " + specId));

        // 只有草稿状态才能发布
        if (!Objects.equals(spec.getStatus(), SpecStatusEnum.DRAFT.getCode())) {
            throw new IllegalArgumentException("只有草稿状态的规格才能发布");
        }

        // 验证规格内容完整性
        validateSpecForPublish(spec);

        spec.setStatus(SpecStatusEnum.PUBLISHED.getCode());
        log.info("规格已发布: {} (ID: {})", spec.getName(), specId);

        return spec;
    }

    /**
     * 归档规格
     *
     * @param specId 规格ID
     * @return 更新后的规格
     * @throws IllegalArgumentException 规格不存在或状态不允许归档
     */
    public Spec archiveSpec(Long specId) {
        Spec spec = specRepository.findById(specId)
                .orElseThrow(() -> new IllegalArgumentException("规格不存在: " + specId));

        // 只有已发布状态才能归档
        if (!Objects.equals(spec.getStatus(), SpecStatusEnum.PUBLISHED.getCode())) {
            throw new IllegalArgumentException("只有已发布状态的规格才能归档");
        }

        spec.setStatus(SpecStatusEnum.ARCHIVED.getCode());
        log.info("规格已归档: {} (ID: {})", spec.getName(), specId);

        return spec;
    }

    /**
     * 验证规格是否可以发布
     *
     * @param spec 规格实体
     */
    private void validateSpecForPublish(Spec spec) {
        validateSpec(spec);

        if (spec.getSystemPrompt() == null || spec.getSystemPrompt().trim().isEmpty()) {
            throw new IllegalArgumentException("发布前必须设置系统提示词");
        }

        if (spec.getDescription() == null || spec.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("发布前必须设置描述");
        }
    }

    /**
     * 检查规格是否可以被修改
     *
     * @param specId 规格ID
     * @return 是否可以修改
     */
    public boolean canModify(Long specId) {
        Spec spec = specRepository.findById(specId).orElse(null);
        if (spec == null) {
            return false;
        }
        // 草稿状态可以修改，已发布和已归档状态不能修改
        return Objects.equals(spec.getStatus(), SpecStatusEnum.DRAFT.getCode());
    }
}
