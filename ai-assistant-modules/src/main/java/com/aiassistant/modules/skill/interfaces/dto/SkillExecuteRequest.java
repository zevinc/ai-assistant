package com.aiassistant.modules.skill.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 技能执行请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillExecuteRequest {

    /**
     * 技能ID
     */
    private Long skillId;

    /**
     * 执行参数
     */
    private Map<String, Object> params;
}
