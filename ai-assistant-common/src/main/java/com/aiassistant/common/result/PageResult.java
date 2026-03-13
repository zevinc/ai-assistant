package com.aiassistant.common.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应结果
 * @param <T> 数据类型
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageResult<T> extends Result<List<T>> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 当前页码
     */
    private int pageNum;
    
    /**
     * 每页大小
     */
    private int pageSize;
    
    /**
     * 总记录数
     */
    private long total;
    
    /**
     * 总页数
     */
    private int pages;
    
    public PageResult() {
        super();
    }
    
    /**
     * 创建分页结果
     * @param data 数据列表
     * @param total 总记录数
     * @param pageNum 当前页码
     * @param pageSize 每页大小
     * @param <T> 数据类型
     * @return 分页结果
     */
    public static <T> PageResult<T> of(List<T> data, long total, int pageNum, int pageSize) {
        PageResult<T> result = new PageResult<>();
        result.setCode(0);
        result.setMessage("success");
        result.setData(data);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotal(total);
        result.setPages((int) Math.ceil((double) total / pageSize));
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }
}
