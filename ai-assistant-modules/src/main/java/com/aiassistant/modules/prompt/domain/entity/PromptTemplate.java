package com.aiassistant.modules.prompt.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Prompt模板实体
 * 用于存储Prompt模板信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_prompt_template")
public class PromptTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 模板名称
     */
    @TableField("name")
    private String name;

    /**
     * 模板描述
     */
    @TableField("description")
    private String description;

    /**
     * 分类
     */
    @TableField("category")
    private String category;

    /**
     * 模板内容
     */
    @TableField("template")
    private String template;

    /**
     * 变量列表（JSON数组格式）
     */
    @TableField("variables")
    private String variables;

    /**
     * 版本号
     */
    @TableField("version")
    private Integer version;

    /**
     * 规格ID
     */
    @TableField("spec_id")
    private Long specId;

    /**
     * 状态（0-草稿, 1-激活, 2-停用）
     */
    @TableField("status")
    private Integer status;
}
