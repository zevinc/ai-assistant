package com.aiassistant.modules.spec.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * AI助手规格/人格定义实体
 * 定义AI助手的行为规范、提示词模板和模型参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_spec", autoResultMap = true)
public class Spec extends BaseEntity {

    /**
     * 规格名称
     */
    private String name;

    /**
     * 规格描述
     */
    private String description;

    /**
     * 系统提示词（JSON格式存储）
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private String systemPrompt;

    /**
     * 温度参数（0.0-2.0，控制输出随机性）
     */
    private Double temperature;

    /**
     * 最大令牌数
     */
    private Integer maxTokens;

    /**
     * 关联的模型ID
     */
    private String modelId;

    /**
     * 状态：0-草稿，1-已发布，2-已归档
     */
    private Integer status;
}
