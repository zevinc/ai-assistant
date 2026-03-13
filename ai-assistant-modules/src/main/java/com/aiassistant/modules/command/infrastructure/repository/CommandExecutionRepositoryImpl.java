package com.aiassistant.modules.command.infrastructure.repository;

import com.aiassistant.modules.command.domain.entity.CommandExecution;
import com.aiassistant.modules.command.domain.repository.CommandExecutionRepository;
import com.aiassistant.modules.command.infrastructure.mapper.CommandExecutionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 命令执行仓储实现
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CommandExecutionRepositoryImpl implements CommandExecutionRepository {

    private final CommandExecutionMapper commandExecutionMapper;

    @Override
    public Optional<CommandExecution> findById(Long id) {
        return Optional.ofNullable(commandExecutionMapper.selectById(id));
    }

    @Override
    public List<CommandExecution> findByCommandId(Long commandId) {
        LambdaQueryWrapper<CommandExecution> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommandExecution::getCommandId, commandId)
                .orderByDesc(CommandExecution::getCreatedAt);
        return commandExecutionMapper.selectList(wrapper);
    }

    @Override
    public List<CommandExecution> findByUserId(Long userId) {
        LambdaQueryWrapper<CommandExecution> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommandExecution::getUserId, userId)
                .orderByDesc(CommandExecution::getCreatedAt);
        return commandExecutionMapper.selectList(wrapper);
    }

    @Override
    public List<CommandExecution> findByCommandIdAndUserId(Long commandId, Long userId) {
        LambdaQueryWrapper<CommandExecution> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommandExecution::getCommandId, commandId)
                .eq(CommandExecution::getUserId, userId)
                .orderByDesc(CommandExecution::getCreatedAt);
        return commandExecutionMapper.selectList(wrapper);
    }

    @Override
    public List<CommandExecution> findAll() {
        LambdaQueryWrapper<CommandExecution> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(CommandExecution::getCreatedAt);
        return commandExecutionMapper.selectList(wrapper);
    }

    @Override
    public CommandExecution save(CommandExecution execution) {
        commandExecutionMapper.insert(execution);
        return execution;
    }

    @Override
    public CommandExecution update(CommandExecution execution) {
        commandExecutionMapper.updateById(execution);
        return execution;
    }
}
