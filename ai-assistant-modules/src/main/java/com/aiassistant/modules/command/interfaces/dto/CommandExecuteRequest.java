package com.aiassistant.modules.command.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 命令执行请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandExecuteRequest {

    /**
     * 命令名称
     */
    private String commandName;

    /**
     * 输入内容
     */
    private String input;

    /**
     * 用户ID
     */
    private Long userId;
}
