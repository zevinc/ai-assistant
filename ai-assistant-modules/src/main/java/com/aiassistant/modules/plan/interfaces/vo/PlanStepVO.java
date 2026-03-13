package com.aiassistant.modules.plan.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 计划步骤视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanStepVO {

    /**
     * 步骤ID
     */
    private Long id;

    /**
     * 所属计划ID
     */
    private Long planId;

    /**
     * 步骤索引
     */
    private Integer stepIndex;

    /**
     * 步骤名称
     */
    private String name;

    /**
     * 步骤描述
     */
    private String description;

    /**
     * 执行动作
     */
    private String action;

    /**
     * 输入参数
     */
    private String input;

    /**
     * 输出结果
     */
    private String output;

    /**
     * 步骤状态
     */
    private String status;

    /**
     * 依赖步骤
     */
    private String dependsOn;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
