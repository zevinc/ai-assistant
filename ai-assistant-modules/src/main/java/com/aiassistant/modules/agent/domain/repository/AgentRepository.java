package com.aiassistant.modules.agent.domain.repository;

import com.aiassistant.modules.agent.domain.entity.Agent;

import java.util.List;
import java.util.Optional;

/**
 * Agent仓储接口
 */
public interface AgentRepository {

    /**
     * 根据ID查找Agent
     *
     * @param id Agent ID
     * @return Agent实体
     */
    Optional<Agent> findById(Long id);

    /**
     * 根据名称查找Agent
     *
     * @param name Agent名称
     * @return Agent实体
     */
    Optional<Agent> findByName(String name);

    /**
     * 根据规格ID查找Agent列表
     *
     * @param specId 规格ID
     * @return Agent列表
     */
    List<Agent> findBySpecId(Long specId);

    /**
     * 查找所有Agent
     *
     * @return Agent列表
     */
    List<Agent> listAll();

    /**
     * 保存Agent
     *
     * @param agent Agent实体
     * @return 保存后的Agent
     */
    Agent save(Agent agent);

    /**
     * 更新Agent
     *
     * @param agent Agent实体
     * @return 更新后的Agent
     */
    Agent update(Agent agent);

    /**
     * 根据ID删除Agent
     *
     * @param id Agent ID
     */
    void deleteById(Long id);
}
