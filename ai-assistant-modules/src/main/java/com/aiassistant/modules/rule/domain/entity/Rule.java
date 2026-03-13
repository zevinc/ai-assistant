package com.aiassistant.modules.rule.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 业务规则实体
 * 定义AI行为的业务规则
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_rule")
public class Rule extends BaseEntity {

    /**
     * 规则名称
     */
    private String name;

    /**
     * 规则描述
     */
    private String description;

    /**
     * 规则类型：pre_check, post_check, filter, transform
     */
    private String ruleType;

    /**
     * 规则表达式（SpEL表达式）
     */
    private String expression;

    /**
     * 优先级（数值越小优先级越高）
     */
    private Integer priority;

    /**
     * 关联的规格ID
     */
    private Long specId;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
}
