package com.aiassistant.modules.command.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 命令实体
 * 定义用户可执行的命令
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_command")
public class Command extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 命令名称
     */
    private String name;

    /**
     * 命令描述
     */
    private String description;

    /**
     * 命令匹配模式（正则表达式）
     */
    private String pattern;

    /**
     * 处理器类名
     */
    private String handlerClass;

    /**
     * 关联的规格ID
     */
    private Long specId;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;
}
