package com.aiassistant.modules.skill.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 技能视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 技能名称
     */
    private String name;

    /**
     * 技能描述
     */
    private String description;

    /**
     * 技能类型
     */
    private String skillType;

    /**
     * 调用端点
     */
    private String endpoint;

    /**
     * 输入参数Schema
     */
    private String inputSchema;

    /**
     * 输出参数Schema
     */
    private String outputSchema;

    /**
     * 超时时间（毫秒）
     */
    private Integer timeout;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 关联的规格ID
     */
    private Long specId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 从实体转换为VO
     */
    public static SkillVO fromEntity(com.aiassistant.modules.skill.domain.entity.Skill skill) {
        return SkillVO.builder()
                .id(skill.getId())
                .name(skill.getName())
                .description(skill.getDescription())
                .skillType(skill.getSkillType())
                .endpoint(skill.getEndpoint())
                .inputSchema(skill.getInputSchema())
                .outputSchema(skill.getOutputSchema())
                .timeout(skill.getTimeout())
                .retryCount(skill.getRetryCount())
                .specId(skill.getSpecId())
                .status(skill.getStatus())
                .createdAt(skill.getCreatedAt())
                .updatedAt(skill.getUpdatedAt())
                .createdBy(skill.getCreatedBy())
                .updatedBy(skill.getUpdatedBy())
                .build();
    }
}
