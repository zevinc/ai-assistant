package com.aiassistant.modules.skill.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 技能类型枚举
 */
@Getter
@AllArgsConstructor
public enum SkillTypeEnum {

    /**
     * HTTP API类型
     */
    HTTP_API("http_api", "HTTP API调用"),

    /**
     * 函数类型
     */
    FUNCTION("function", "本地函数调用"),

    /**
     * MCP工具类型
     */
    MCP_TOOL("mcp_tool", "MCP工具调用"),

    /**
     * 工作流类型
     */
    WORKFLOW("workflow", "工作流编排");

    private final String code;
    private final String description;

    /**
     * 根据code获取枚举
     */
    public static SkillTypeEnum fromCode(String code) {
        for (SkillTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown skill type code: " + code);
    }
}
