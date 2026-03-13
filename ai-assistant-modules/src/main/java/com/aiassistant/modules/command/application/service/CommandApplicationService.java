package com.aiassistant.modules.command.application.service;

import com.aiassistant.common.event.DomainEventPublisher;
import com.aiassistant.common.event.SimpleDomainEvent;
import com.aiassistant.modules.command.domain.entity.Command;
import com.aiassistant.modules.command.domain.entity.CommandExecution;
import com.aiassistant.modules.command.domain.enums.CommandExecutionStatusEnum;
import com.aiassistant.modules.command.domain.repository.CommandExecutionRepository;
import com.aiassistant.modules.command.domain.repository.CommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 命令应用服务
 * 提供命令的CRUD和执行跟踪功能
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommandApplicationService {

    private final CommandRepository commandRepository;
    private final CommandExecutionRepository commandExecutionRepository;
    private final DomainEventPublisher domainEventPublisher;

    /**
     * 创建命令
     */
    @Transactional
    public Command createCommand(Command command) {
        log.info("Creating command: name={}", command.getName());

        // 检查名称是否已存在
        Optional<Command> existingCommand = commandRepository.findByName(command.getName());
        if (existingCommand.isPresent()) {
            throw new IllegalArgumentException("Command with name '" + command.getName() + "' already exists");
        }

        Command savedCommand = commandRepository.save(command);

        // 发布领域事件
        publishEvent("command.created", savedCommand);

        return savedCommand;
    }

    /**
     * 根据ID查询命令
     */
    public Optional<Command> getCommandById(Long id) {
        return commandRepository.findById(id);
    }

    /**
     * 根据名称查询命令
     */
    public Optional<Command> getCommandByName(String name) {
        return commandRepository.findByName(name);
    }

    /**
     * 查询所有命令
     */
    public List<Command> getAllCommands() {
        return commandRepository.findAll();
    }

    /**
     * 根据规格ID查询命令
     */
    public List<Command> getCommandsBySpecId(Long specId) {
        return commandRepository.findBySpecId(specId);
    }

    /**
     * 更新命令
     */
    @Transactional
    public Command updateCommand(Long id, Command command) {
        log.info("Updating command: id={}", id);

        Command existingCommand = commandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Command not found with id: " + id));

        // 更新字段
        if (command.getName() != null) {
            existingCommand.setName(command.getName());
        }
        if (command.getDescription() != null) {
            existingCommand.setDescription(command.getDescription());
        }
        if (command.getPattern() != null) {
            existingCommand.setPattern(command.getPattern());
        }
        if (command.getHandlerClass() != null) {
            existingCommand.setHandlerClass(command.getHandlerClass());
        }
        if (command.getSpecId() != null) {
            existingCommand.setSpecId(command.getSpecId());
        }
        if (command.getStatus() != null) {
            existingCommand.setStatus(command.getStatus());
        }

        Command updatedCommand = commandRepository.update(existingCommand);

        // 发布领域事件
        publishEvent("command.updated", updatedCommand);

        return updatedCommand;
    }

    /**
     * 删除命令
     */
    @Transactional
    public void deleteCommand(Long id) {
        log.info("Deleting command: id={}", id);

        Command command = commandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Command not found with id: " + id));

        commandRepository.deleteById(id);

        // 发布领域事件
        publishEvent("command.deleted", command);
    }

    /**
     * 执行命令
     */
    @Transactional
    public CommandExecution executeCommand(String commandName, String input, Long userId) {
        log.info("Executing command: name={}, userId={}", commandName, userId);

        // 查找命令
        Command command = commandRepository.findByName(commandName)
                .orElseThrow(() -> new IllegalArgumentException("Command not found with name: " + commandName));

        // 检查命令状态
        if (command.getStatus() == null || command.getStatus() != 1) {
            throw new IllegalStateException("Command is not enabled: " + commandName);
        }

        // 创建执行记录
        CommandExecution execution = CommandExecution.builder()
                .commandId(command.getId())
                .userId(userId)
                .input(input)
                .status(CommandExecutionStatusEnum.RUNNING.getCode())
                .build();

        execution = commandExecutionRepository.save(execution);

        long startTime = System.currentTimeMillis();

        try {
            // 执行命令处理
            String output = doExecuteCommand(command, input);

            // 更新执行记录为成功
            long executionTime = System.currentTimeMillis() - startTime;
            execution.setOutput(output);
            execution.setExecutionTime(executionTime);
            execution.setStatus(CommandExecutionStatusEnum.SUCCESS.getCode());
            execution = commandExecutionRepository.update(execution);

            // 发布领域事件
            publishExecutionEvent("command.execution.success", execution);

            return execution;

        } catch (Exception e) {
            log.error("Command execution failed: commandName={}, error={}", commandName, e.getMessage(), e);

            // 更新执行记录为失败
            long executionTime = System.currentTimeMillis() - startTime;
            execution.setOutput("Error: " + e.getMessage());
            execution.setExecutionTime(executionTime);
            execution.setStatus(CommandExecutionStatusEnum.FAILED.getCode());
            execution = commandExecutionRepository.update(execution);

            // 发布领域事件
            publishExecutionEvent("command.execution.failed", execution);

            return execution;
        }
    }

    /**
     * 实际执行命令逻辑
     */
    private String doExecuteCommand(Command command, String input) {
        log.info("Executing command handler: handlerClass={}", command.getHandlerClass());

        // TODO: 根据handlerClass反射调用对应的处理器
        // 这里提供默认实现
        StringBuilder output = new StringBuilder();
        output.append("Command executed successfully.\n");
        output.append("Command: ").append(command.getName()).append("\n");
        output.append("Pattern: ").append(command.getPattern()).append("\n");
        output.append("Input: ").append(input).append("\n");
        output.append("Handler: ").append(command.getHandlerClass()).append("\n");

        return output.toString();
    }

    /**
     * 查询命令执行历史
     */
    public List<CommandExecution> getExecutionHistory(Long commandId, Long userId) {
        if (commandId != null && userId != null) {
            return commandExecutionRepository.findByCommandIdAndUserId(commandId, userId);
        } else if (commandId != null) {
            return commandExecutionRepository.findByCommandId(commandId);
        } else if (userId != null) {
            return commandExecutionRepository.findByUserId(userId);
        }
        return commandExecutionRepository.findAll();
    }

    /**
     * 根据ID查询执行记录
     */
    public Optional<CommandExecution> getExecutionById(Long id) {
        return commandExecutionRepository.findById(id);
    }

    /**
     * 发布命令领域事件
     */
    private void publishEvent(String eventType, Command command) {
        try {
            domainEventPublisher.publish(new SimpleDomainEvent(String.valueOf(command.getId()), "Command", eventType));
        } catch (Exception e) {
            log.warn("Failed to publish domain event: {}", e.getMessage());
        }
    }

    /**
     * 发布执行记录领域事件
     */
    private void publishExecutionEvent(String eventType, CommandExecution execution) {
        try {
            domainEventPublisher.publish(new SimpleDomainEvent(String.valueOf(execution.getId()), "CommandExecution", eventType));
        } catch (Exception e) {
            log.warn("Failed to publish domain event: {}", e.getMessage());
        }
    }
}
