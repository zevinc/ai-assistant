package com.aiassistant.modules.plan.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计划步骤更新请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanStepUpdateRequest {

    /**
     * 步骤ID
     */
    private Long stepId;

    /**
     * 步骤状态（pending/running/completed/failed）
     */
    private String status;

    /**
     * 输出结果
     */
    private String output;
}
