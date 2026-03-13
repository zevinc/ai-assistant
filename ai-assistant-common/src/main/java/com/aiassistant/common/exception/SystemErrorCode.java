package com.aiassistant.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统错误码枚举
 * 定义系统级别的通用错误码
 */
@Getter
@AllArgsConstructor
public enum SystemErrorCode implements ErrorCode {
    
    /**
     * 成功
     */
    SUCCESS(0, "success"),
    
    /**
     * 系统内部错误
     */
    SYSTEM_ERROR(500, "系统内部错误"),
    
    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),
    
    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),
    
    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),
    
    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问");
    
    private final int code;
    private final String message;
}
