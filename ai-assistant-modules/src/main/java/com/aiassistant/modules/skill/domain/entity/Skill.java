package com.aiassistant.modules.skill.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 技能实体
 * 定义可执行的技能、工具或插件
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_skill")
public class Skill extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 技能名称
     */
    private String name;

    /**
     * 技能描述
     */
    private String description;

    /**
     * 技能类型：http_api, function, mcp_tool, workflow
     */
    private String skillType;

    /**
     * 调用端点（HTTP API地址或函数类名）
     */
    private String endpoint;

    /**
     * 输入参数Schema（JSON格式）
     */
    private String inputSchema;

    /**
     * 输出参数Schema（JSON格式）
     */
    private String outputSchema;

    /**
     * 超时时间（毫秒）
     */
    @Builder.Default
    private Integer timeout = 30000;

    /**
     * 重试次数
     */
    @Builder.Default
    private Integer retryCount = 0;

    /**
     * 关联的规格ID
     */
    private Long specId;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;
}
