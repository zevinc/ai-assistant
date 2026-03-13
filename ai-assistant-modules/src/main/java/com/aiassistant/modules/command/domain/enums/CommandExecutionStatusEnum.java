package com.aiassistant.modules.command.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 命令执行状态枚举
 */
@Getter
@AllArgsConstructor
public enum CommandExecutionStatusEnum {

    /**
     * 待执行
     */
    PENDING("pending", "待执行"),

    /**
     * 执行中
     */
    RUNNING("running", "执行中"),

    /**
     * 执行成功
     */
    SUCCESS("success", "执行成功"),

    /**
     * 执行失败
     */
    FAILED("failed", "执行失败"),

    /**
     * 执行超时
     */
    TIMEOUT("timeout", "执行超时");

    private final String code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static CommandExecutionStatusEnum fromCode(String code) {
        for (CommandExecutionStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown command execution status code: " + code);
    }
}
