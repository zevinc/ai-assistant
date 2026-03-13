package com.aiassistant.modules.plan.domain.repository;

import com.aiassistant.modules.plan.domain.entity.Plan;

import java.util.List;
import java.util.Optional;

/**
 * 计划仓储接口
 */
public interface PlanRepository {

    /**
     * 根据ID查找计划
     *
     * @param id 计划ID
     * @return 计划实体
     */
    Optional<Plan> findById(Long id);

    /**
     * 根据Agent ID查找计划列表
     *
     * @param agentId Agent ID
     * @return 计划列表
     */
    List<Plan> findByAgentId(Long agentId);

    /**
     * 根据会话ID查找计划列表
     *
     * @param sessionId 会话ID
     * @return 计划列表
     */
    List<Plan> findBySessionId(String sessionId);

    /**
     * 保存计划
     *
     * @param plan 计划实体
     * @return 保存后的计划
     */
    Plan save(Plan plan);

    /**
     * 更新计划
     *
     * @param plan 计划实体
     * @return 更新后的计划
     */
    Plan update(Plan plan);

    /**
     * 根据ID删除计划
     *
     * @param id 计划ID
     */
    void deleteById(Long id);
}
