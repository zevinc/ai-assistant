package com.aiassistant.modules.plan.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 计划创建请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanCreateRequest {

    /**
     * 计划名称
     */
    private String name;

    /**
     * 计划描述
     */
    private String description;

    /**
     * Agent ID
     */
    private Long agentId;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 执行策略（react/plan_execute/hierarchical）
     */
    private String strategy;
}
