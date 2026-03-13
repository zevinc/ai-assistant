package com.aiassistant.common.exception;

import lombok.Getter;

/**
 * 业务异常
 * 用于业务逻辑中抛出的异常
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误码
     */
    private final ErrorCode errorCode;
    
    /**
     * 自定义错误消息（可选，用于覆盖默认消息）
     */
    private final String customMessage;
    
    /**
     * 构造业务异常
     * @param errorCode 错误码
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.customMessage = null;
    }
    
    /**
     * 构造业务异常，使用自定义消息
     * @param errorCode 错误码
     * @param message 自定义错误消息
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.customMessage = message;
    }
    
    /**
     * 构造业务异常，带原因
     * @param errorCode 错误码
     * @param cause 原因
     */
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.customMessage = null;
    }
    
    /**
     * 构造业务异常，使用自定义消息和原因
     * @param errorCode 错误码
     * @param message 自定义错误消息
     * @param cause 原因
     */
    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.customMessage = message;
    }
    
    /**
     * 获取错误消息
     * 如果有自定义消息则返回自定义消息，否则返回错误码默认消息
     * @return 错误消息
     */
    @Override
    public String getMessage() {
        return customMessage != null ? customMessage : errorCode.getMessage();
    }
}
