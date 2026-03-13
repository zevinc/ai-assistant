package com.aiassistant.modules.agent.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agent创建请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentCreateRequest {

    /**
     * Agent名称
     */
    private String name;

    /**
     * Agent描述
     */
    private String description;

    /**
     * 规格ID
     */
    private Long specId;

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 系统提示词
     */
    private String systemPrompt;

    /**
     * 温度参数
     */
    private Double temperature;

    /**
     * 最大Token数
     */
    private Integer maxTokens;

    /**
     * 是否启用记忆
     */
    private Boolean enableMemory;

    /**
     * 是否启用规划
     */
    private Boolean enablePlanning;

    /**
     * 是否启用工具
     */
    private Boolean enableTools;
}
