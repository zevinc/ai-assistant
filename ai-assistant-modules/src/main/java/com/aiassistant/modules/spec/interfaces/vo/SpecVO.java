package com.aiassistant.modules.spec.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 规格视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecVO {

    /**
     * 规格ID
     */
    private Long id;

    /**
     * 规格名称
     */
    private String name;

    /**
     * 规格描述
     */
    private String description;

    /**
     * 系统提示词
     */
    private String systemPrompt;

    /**
     * 温度参数
     */
    private Double temperature;

    /**
     * 最大令牌数
     */
    private Integer maxTokens;

    /**
     * 模型ID
     */
    private String modelId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
