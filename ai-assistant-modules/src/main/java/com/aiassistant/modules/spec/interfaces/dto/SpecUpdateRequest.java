package com.aiassistant.modules.spec.interfaces.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 规格更新请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecUpdateRequest {

    /**
     * 规格ID
     */
    @NotNull(message = "规格ID不能为空")
    private Long id;

    /**
     * 规格名称
     */
    @NotBlank(message = "规格名称不能为空")
    private String name;

    /**
     * 规格描述
     */
    @NotBlank(message = "规格描述不能为空")
    private String description;

    /**
     * 系统提示词
     */
    @NotBlank(message = "系统提示词不能为空")
    private String systemPrompt;

    /**
     * 温度参数（0.0-2.0）
     */
    @NotNull(message = "温度参数不能为空")
    @Min(value = 0, message = "温度参数最小值为0")
    @Max(value = 2, message = "温度参数最大值为2")
    private Double temperature;

    /**
     * 最大令牌数
     */
    @NotNull(message = "最大令牌数不能为空")
    @Min(value = 1, message = "最大令牌数必须大于0")
    private Integer maxTokens;

    /**
     * 模型ID
     */
    @NotBlank(message = "模型ID不能为空")
    private String modelId;
}
