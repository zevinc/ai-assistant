package com.aiassistant.modules.agent.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Agent视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentVO {

    /**
     * Agent ID
     */
    private Long id;

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

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
