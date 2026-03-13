package com.aiassistant.modules.command.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 命令执行视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandExecutionVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 命令ID
     */
    private Long commandId;

    /**
     * 命令名称
     */
    private String commandName;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 输入内容
     */
    private String input;

    /**
     * 输出内容
     */
    private String output;

    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;

    /**
     * 执行状态
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 从实体转换为VO
     */
    public static CommandExecutionVO fromEntity(com.aiassistant.modules.command.domain.entity.CommandExecution execution) {
        return CommandExecutionVO.builder()
                .id(execution.getId())
                .commandId(execution.getCommandId())
                .userId(execution.getUserId())
                .input(execution.getInput())
                .output(execution.getOutput())
                .executionTime(execution.getExecutionTime())
                .status(execution.getStatus())
                .createdAt(execution.getCreatedAt())
                .updatedAt(execution.getUpdatedAt())
                .build();
    }

    /**
     * 从实体转换为VO（包含命令名称）
     */
    public static CommandExecutionVO fromEntity(com.aiassistant.modules.command.domain.entity.CommandExecution execution,
                                                 String commandName) {
        return CommandExecutionVO.builder()
                .id(execution.getId())
                .commandId(execution.getCommandId())
                .commandName(commandName)
                .userId(execution.getUserId())
                .input(execution.getInput())
                .output(execution.getOutput())
                .executionTime(execution.getExecutionTime())
                .status(execution.getStatus())
                .createdAt(execution.getCreatedAt())
                .updatedAt(execution.getUpdatedAt())
                .build();
    }
}
