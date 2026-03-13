package com.aiassistant.modules.command.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 命令执行实体
 * 记录命令执行的历史和状态
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_command_execution")
public class CommandExecution extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 命令ID
     */
    private Long commandId;

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
}
