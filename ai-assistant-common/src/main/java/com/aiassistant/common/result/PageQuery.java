package com.aiassistant.common.result;

import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.io.Serializable;

/**
 * 分页查询请求基类
 */
@Data
public class PageQuery implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 默认页码
     */
    private static final int DEFAULT_PAGE_NUM = 1;
    
    /**
     * 默认每页大小
     */
    private static final int DEFAULT_PAGE_SIZE = 10;
    
    /**
     * 最大每页大小
     */
    private static final int MAX_PAGE_SIZE = 100;
    
    /**
     * 当前页码，从1开始
     */
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = DEFAULT_PAGE_NUM;
    
    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小最小为1")
    @Max(value = 100, message = "每页大小最大为100")
    private Integer pageSize = DEFAULT_PAGE_SIZE;
    
    /**
     * 获取偏移量（用于数据库查询）
     * @return 偏移量
     */
    public int getOffset() {
        return (pageNum - 1) * pageSize;
    }
    
    /**
     * 获取安全的每页大小
     * 如果设置的值超过最大值，则返回最大值
     * @return 安全的每页大小
     */
    public int getSafePageSize() {
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }
}
