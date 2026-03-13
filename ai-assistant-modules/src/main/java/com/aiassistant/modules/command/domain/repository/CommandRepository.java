package com.aiassistant.modules.command.domain.repository;

import com.aiassistant.modules.command.domain.entity.Command;

import java.util.List;
import java.util.Optional;

/**
 * 命令仓储接口
 */
public interface CommandRepository {

    /**
     * 根据ID查询命令
     */
    Optional<Command> findById(Long id);

    /**
     * 根据名称查询命令
     */
    Optional<Command> findByName(String name);

    /**
     * 根据规格ID查询命令列表
     */
    List<Command> findBySpecId(Long specId);

    /**
     * 查询所有命令
     */
    List<Command> findAll();

    /**
     * 保存命令
     */
    Command save(Command command);

    /**
     * 更新命令
     */
    Command update(Command command);

    /**
     * 根据ID删除命令
     */
    void deleteById(Long id);
}
