package com.aiassistant.modules.command.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 命令视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 命令名称
     */
    private String name;

    /**
     * 命令描述
     */
    private String description;

    /**
     * 命令匹配模式
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
     * 状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 从实体转换为VO
     */
    public static CommandVO fromEntity(com.aiassistant.modules.command.domain.entity.Command command) {
        return CommandVO.builder()
                .id(command.getId())
                .name(command.getName())
                .description(command.getDescription())
                .pattern(command.getPattern())
                .handlerClass(command.getHandlerClass())
                .specId(command.getSpecId())
                .status(command.getStatus())
                .createdAt(command.getCreatedAt())
                .updatedAt(command.getUpdatedAt())
                .createdBy(command.getCreatedBy())
                .updatedBy(command.getUpdatedBy())
                .build();
    }
}
