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
 * 计划实体
 * 用于存储任务计划和分解信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_plan")
public class Plan extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 计划名称
     */
    @TableField("name")
    private String name;

    /**
     * 计划描述
     */
    @TableField("description")
    private String description;

    /**
     * Agent ID
     */
    @TableField("agent_id")
    private Long agentId;

    /**
     * 会话ID
     */
    @TableField("session_id")
    private String sessionId;

    /**
     * 执行策略（react/plan_execute/hierarchical）
     */
    @TableField("strategy")
    private String strategy;

    /**
     * 计划状态（created/running/paused/completed/failed）
     */
    @TableField("status")
    private String status;

    /**
     * 总步骤数
     */
    @TableField("total_steps")
    private Integer totalSteps;

    /**
     * 已完成步骤数
     */
    @TableField("completed_steps")
    private Integer completedSteps;
}
