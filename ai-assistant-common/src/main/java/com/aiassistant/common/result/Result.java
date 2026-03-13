package com.aiassistant.common.result;

import com.aiassistant.common.exception.ErrorCode;
import com.aiassistant.common.exception.SystemErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果包装类
 * @param <T> 数据类型
 */
@Data
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 响应码
     */
    private int code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 成功响应（无数据）
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> ok() {
        return new Result<>(SystemErrorCode.SUCCESS.getCode(), SystemErrorCode.SUCCESS.getMessage(), null);
    }
    
    /**
     * 成功响应（带数据）
     * @param data 数据
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(SystemErrorCode.SUCCESS.getCode(), SystemErrorCode.SUCCESS.getMessage(), data);
    }
    
    /**
     * 成功响应（带数据和消息）
     * @param data 数据
     * @param message 消息
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> ok(T data, String message) {
        return new Result<>(SystemErrorCode.SUCCESS.getCode(), message, data);
    }
    
    /**
     * 失败响应（使用错误码）
     * @param errorCode 错误码
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> fail(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }
    
    /**
     * 失败响应（使用错误码和自定义消息）
     * @param errorCode 错误码
     * @param message 自定义消息
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> fail(ErrorCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), message, null);
    }
    
    /**
     * 失败响应（使用错误码和消息）
     * @param code 错误码
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 失败结果
     */
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }
    
    /**
     * 判断是否成功
     * @return 是否成功
     */
    public boolean isSuccess() {
        return SystemErrorCode.SUCCESS.getCode() == this.code;
    }
}
