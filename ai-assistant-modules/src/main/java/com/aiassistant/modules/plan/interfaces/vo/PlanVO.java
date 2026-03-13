package com.aiassistant.modules.plan.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 计划视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanVO {

    /**
     * 计划ID
     */
    private Long id;

    /**
     * 计划名称
     */
    private String name;

    /**
     * 计划描述
     */
    private String description;

    /**
     * Agent ID
     */
    private Long agentId;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 执行策略
     */
    private String strategy;

    /**
     * 计划状态
     */
    private String status;

    /**
     * 总步骤数
     */
    private Integer totalSteps;

    /**
     * 已完成步骤数
     */
    private Integer completedSteps;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
