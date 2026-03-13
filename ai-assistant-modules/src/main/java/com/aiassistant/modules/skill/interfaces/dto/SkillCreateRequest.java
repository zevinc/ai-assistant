package com.aiassistant.modules.skill.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 技能创建请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillCreateRequest {

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
     * 调用端点
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
     * 状态（0-禁用，1-启用）
     */
    private Integer status;
}
