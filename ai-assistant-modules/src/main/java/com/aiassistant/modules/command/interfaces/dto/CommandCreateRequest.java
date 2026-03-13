package com.aiassistant.modules.command.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 命令创建请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandCreateRequest {

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
