package com.aiassistant.modules.agent.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Agent实体
 * 用于存储Agent配置信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_agent")
public class Agent extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Agent名称
     */
    @TableField("name")
    private String name;

    /**
     * Agent描述
     */
    @TableField("description")
    private String description;

    /**
     * 规格ID
     */
    @TableField("spec_id")
    private Long specId;

    /**
     * 模型ID
     */
    @TableField("model_id")
    private String modelId;

    /**
     * 系统提示词
     */
    @TableField("system_prompt")
    private String systemPrompt;

    /**
     * 温度参数
     */
    @TableField("temperature")
    private Double temperature;

    /**
     * 最大Token数
     */
    @TableField("max_tokens")
    private Integer maxTokens;

    /**
     * 是否启用记忆
     */
    @TableField("enable_memory")
    private Boolean enableMemory;

    /**
     * 是否启用规划
     */
    @TableField("enable_planning")
    private Boolean enablePlanning;

    /**
     * 是否启用工具
     */
    @TableField("enable_tools")
    private Boolean enableTools;

    /**
     * 状态（0-草稿, 1-激活, 2-停用）
     */
    @TableField("status")
    private Integer status;
}
