package com.aiassistant.modules.plan.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 计划步骤实体
 * 用于存储计划的各个执行步骤
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_plan_step")
public class PlanStep extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 所属计划ID
     */
    @TableField("plan_id")
    private Long planId;

    /**
     * 步骤索引
     */
    @TableField("step_index")
    private Integer stepIndex;

    /**
     * 步骤名称
     */
    @TableField("name")
    private String name;

    /**
     * 步骤描述
     */
    @TableField("description")
    private String description;

    /**
     * 执行动作
     */
    @TableField("action")
    private String action;

    /**
     * 输入参数（JSON格式）
     */
    @TableField("input")
    private String input;

    /**
     * 输出结果（JSON格式）
     */
    @TableField("output")
    private String output;

    /**
     * 步骤状态（pending/running/completed/failed）
     */
    @TableField("status")
    private String status;

    /**
     * 依赖步骤ID列表（逗号分隔）
     */
    @TableField("depends_on")
    private String dependsOn;
}
