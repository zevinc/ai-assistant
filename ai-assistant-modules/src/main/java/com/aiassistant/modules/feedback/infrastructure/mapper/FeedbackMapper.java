package com.aiassistant.modules.feedback.infrastructure.mapper;

import com.aiassistant.modules.feedback.domain.entity.Feedback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 反馈Mapper接口
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {

    /**
     * 统计Agent的反馈数量
     *
     * @param agentId Agent ID
     * @return 反馈数量
     */
    @Select("SELECT COUNT(*) FROM t_feedback WHERE agent_id = #{agentId} AND is_deleted = 0")
    long countByAgentId(@Param("agentId") Long agentId);

    /**
     * 计算Agent的平均评分
     *
     * @param agentId Agent ID
     * @return 平均评分
     */
    @Select("SELECT AVG(rating) FROM t_feedback WHERE agent_id = #{agentId} AND rating IS NOT NULL AND is_deleted = 0")
    Double avgRatingByAgentId(@Param("agentId") Long agentId);
}
