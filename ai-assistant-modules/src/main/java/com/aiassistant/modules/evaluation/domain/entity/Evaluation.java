package com.aiassistant.modules.evaluation.domain.entity;

import com.aiassistant.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 评估实体
 * 用于存储AI响应质量评估信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_evaluation")
public class Evaluation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Agent ID
     */
    @TableField("agent_id")
    private Long agentId;

    /**
     * 会话ID
     */
    @TableField("session_id")
    private String sessionId;

    /**
     * 输入文本
     */
    @TableField("input_text")
    private String inputText;

    /**
     * 输出文本
     */
    @TableField("output_text")
    private String outputText;

    /**
     * 期望输出
     */
    @TableField("expected_output")
    private String expectedOutput;

    /**
     * 评分
     */
    @TableField("score")
    private Double score;

    /**
     * 评估指标（JSON格式）
     */
    @TableField("metrics")
    private String metrics;

    /**
     * 评估类型
     */
    @TableField("evaluation_type")
    private String evaluationType;

    /**
     * 状态（0-待评估, 1-已完成）
     */
    @TableField("status")
    private Integer status;
}
