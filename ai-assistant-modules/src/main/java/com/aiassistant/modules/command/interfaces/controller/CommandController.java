package com.aiassistant.modules.command.interfaces.controller;

import com.aiassistant.common.result.Result;
import com.aiassistant.modules.command.application.service.CommandApplicationService;
import com.aiassistant.modules.command.domain.entity.Command;
import com.aiassistant.modules.command.domain.entity.CommandExecution;
import com.aiassistant.modules.command.interfaces.dto.CommandCreateRequest;
import com.aiassistant.modules.command.interfaces.dto.CommandExecuteRequest;
import com.aiassistant.modules.command.interfaces.vo.CommandExecutionVO;
import com.aiassistant.modules.command.interfaces.vo.CommandVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 命令控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/commands")
@RequiredArgsConstructor
public class CommandController {

    private final CommandApplicationService commandApplicationService;

    /**
     * 创建命令
     */
    @PostMapping
    public Result<CommandVO> createCommand(@RequestBody CommandCreateRequest request) {
        log.info("Creating command: name={}", request.getName());

        Command command = Command.builder()
                .name(request.getName())
                .description(request.getDescription())
                .pattern(request.getPattern())
                .handlerClass(request.getHandlerClass())
                .specId(request.getSpecId())
                .status(request.getStatus() != null ? request.getStatus() : 1)
                .build();

        Command createdCommand = commandApplicationService.createCommand(command);
        return Result.ok(CommandVO.fromEntity(createdCommand));
    }

    /**
     * 根据ID查询命令
     */
    @GetMapping("/{id}")
    public Result<CommandVO> getCommandById(@PathVariable Long id) {
        log.info("Getting command by id: {}", id);
        return commandApplicationService.getCommandById(id)
                .map(command -> Result.ok(CommandVO.fromEntity(command)))
                .orElse(Result.fail(404, "Command not found"));
    }

    /**
     * 根据名称查询命令
     */
    @GetMapping("/name/{name}")
    public Result<CommandVO> getCommandByName(@PathVariable String name) {
        log.info("Getting command by name: {}", name);
        return commandApplicationService.getCommandByName(name)
                .map(command -> Result.ok(CommandVO.fromEntity(command)))
                .orElse(Result.fail(404, "Command not found"));
    }

    /**
     * 查询所有命令
     */
    @GetMapping
    public Result<List<CommandVO>> getAllCommands() {
        log.info("Getting all commands");
        List<CommandVO> commands = commandApplicationService.getAllCommands().stream()
                .map(CommandVO::fromEntity)
                .collect(Collectors.toList());
        return Result.ok(commands);
    }

    /**
     * 根据规格ID查询命令
     */
    @GetMapping("/spec/{specId}")
    public Result<List<CommandVO>> getCommandsBySpecId(@PathVariable Long specId) {
        log.info("Getting commands by specId: {}", specId);
        List<CommandVO> commands = commandApplicationService.getCommandsBySpecId(specId).stream()
                .map(CommandVO::fromEntity)
                .collect(Collectors.toList());
        return Result.ok(commands);
    }

    /**
     * 更新命令
     */
    @PutMapping("/{id}")
    public Result<CommandVO> updateCommand(@PathVariable Long id, @RequestBody CommandCreateRequest request) {
        log.info("Updating command: id={}", id);

        Command command = Command.builder()
                .name(request.getName())
                .description(request.getDescription())
                .pattern(request.getPattern())
                .handlerClass(request.getHandlerClass())
                .specId(request.getSpecId())
                .status(request.getStatus())
                .build();

        Command updatedCommand = commandApplicationService.updateCommand(id, command);
        return Result.ok(CommandVO.fromEntity(updatedCommand));
    }

    /**
     * 删除命令
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCommand(@PathVariable Long id) {
        log.info("Deleting command: id={}", id);
        commandApplicationService.deleteCommand(id);
        return Result.ok();
    }

    /**
     * 执行命令
     */
    @PostMapping("/execute")
    public Result<CommandExecutionVO> executeCommand(@RequestBody CommandExecuteRequest request) {
        log.info("Executing command: commandName={}, userId={}", request.getCommandName(), request.getUserId());
        CommandExecution execution = commandApplicationService.executeCommand(
                request.getCommandName(),
                request.getInput(),
                request.getUserId()
        );
        return Result.ok(CommandExecutionVO.fromEntity(execution));
    }

    /**
     * 查询执行历史
     */
    @GetMapping("/history")
    public Result<List<CommandExecutionVO>> getExecutionHistory(
            @RequestParam(required = false) Long commandId,
            @RequestParam(required = false) Long userId) {
        log.info("Getting execution history: commandId={}, userId={}", commandId, userId);
        List<CommandExecutionVO> history = commandApplicationService.getExecutionHistory(commandId, userId).stream()
                .map(CommandExecutionVO::fromEntity)
                .collect(Collectors.toList());
        return Result.ok(history);
    }

    /**
     * 根据ID查询执行记录
     */
    @GetMapping("/execution/{id}")
    public Result<CommandExecutionVO> getExecutionById(@PathVariable Long id) {
        log.info("Getting execution by id: {}", id);
        return commandApplicationService.getExecutionById(id)
                .map(execution -> Result.ok(CommandExecutionVO.fromEntity(execution)))
                .orElse(Result.fail(404, "Execution not found"));
    }
}
