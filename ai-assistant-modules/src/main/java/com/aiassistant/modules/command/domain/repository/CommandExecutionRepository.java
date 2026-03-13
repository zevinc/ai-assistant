package com.aiassistant.modules.command.domain.repository;

import com.aiassistant.modules.command.domain.entity.CommandExecution;

import java.util.List;
import java.util.Optional;

/**
 * 命令执行仓储接口
 */
public interface CommandExecutionRepository {

    /**
     * 根据ID查询命令执行记录
     */
    Optional<CommandExecution> findById(Long id);

    /**
     * 根据命令ID查询执行记录列表
     */
    List<CommandExecution> findByCommandId(Long commandId);

    /**
     * 根据用户ID查询执行记录列表
     */
    List<CommandExecution> findByUserId(Long userId);

    /**
     * 根据命令ID和用户ID查询执行记录列表
     */
    List<CommandExecution> findByCommandIdAndUserId(Long commandId, Long userId);

    /**
     * 查询所有执行记录
     */
    List<CommandExecution> findAll();

    /**
     * 保存命令执行记录
     */
    CommandExecution save(CommandExecution execution);

    /**
     * 更新命令执行记录
     */
    CommandExecution update(CommandExecution execution);
}
