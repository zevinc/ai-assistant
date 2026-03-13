package com.aiassistant.modules.feedback.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 反馈统计视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackStatsVO {

    /**
     * Agent ID
     */
    private Long agentId;

    /**
     * 总反馈数
     */
    private Long totalCount;

    /**
     * 平均评分
     */
    private Double averageRating;
}
