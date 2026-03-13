package com.aiassistant.common.exception;

/**
 * 错误码接口
 * 定义统一的错误码规范
 */
public interface ErrorCode {
    
    /**
     * 获取错误码
     * @return 错误码
     */
    int getCode();
    
    /**
     * 获取错误消息
     * @return 错误消息
     */
    String getMessage();
}
