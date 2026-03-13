package com.aiassistant.modules.evaluation.interfaces.dto;

import lombok.Data;

import java.util.List;

/**
 * 批量评估请求
 */
@Data
public class EvaluationBatchRequest {

    /**
     * 评估项列表
     */
    private List<EvaluationCreateRequest> items;
}
