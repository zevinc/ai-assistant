package com.aiassistant.modules.prompt.infrastructure.mapper;

import com.aiassistant.modules.prompt.domain.entity.PromptTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Prompt模板Mapper接口
 */
@Mapper
public interface PromptTemplateMapper extends BaseMapper<PromptTemplate> {
}
