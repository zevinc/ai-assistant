package com.aiassistant.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

/**
 * Jackson工具类
 * 提供JSON序列化和反序列化的便捷方法
 */
@Slf4j
public final class JacksonUtils {
    
    /**
     * ObjectMapper单例
     */
    private static final ObjectMapper OBJECT_MAPPER;
    
    static {
        OBJECT_MAPPER = new ObjectMapper();
        // 注册Java 8时间模块
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        // 禁用日期时间作为时间戳
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 忽略未知属性
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略空值
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    
    private JacksonUtils() {
        // 私有构造函数，防止实例化
    }
    
    /**
     * 获取ObjectMapper实例
     * @return ObjectMapper实例
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
    
    /**
     * 对象转JSON字符串
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("对象转JSON失败: {}", obj, e);
            throw new RuntimeException("对象转JSON失败", e);
        }
    }
    
    /**
     * JSON字符串转对象
     * @param json JSON字符串
     * @param clazz 目标类型
     * @param <T> 类型参数
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("JSON转对象失败: json={}, class={}", json, clazz.getName(), e);
            throw new RuntimeException("JSON转对象失败", e);
        }
    }
    
    /**
     * JSON字符串转对象（支持泛型）
     * @param json JSON字符串
     * @param typeReference 类型引用
     * @param <T> 类型参数
     * @return 对象
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (Exception e) {
            log.error("JSON转对象失败: json={}, type={}", json, typeReference.getType(), e);
            throw new RuntimeException("JSON转对象失败", e);
        }
    }
    
    /**
     * 对象转字节数组
     * @param obj 对象
     * @return 字节数组
     */
    public static byte[] toJsonBytes(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsBytes(obj);
        } catch (Exception e) {
            log.error("对象转字节数组失败: {}", obj, e);
            throw new RuntimeException("对象转字节数组失败", e);
        }
    }
    
    /**
     * 字节数组转对象
     * @param bytes 字节数组
     * @param clazz 目标类型
     * @param <T> 类型参数
     * @return 对象
     */
    public static <T> T fromJsonBytes(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(bytes, clazz);
        } catch (Exception e) {
            log.error("字节数组转对象失败: class={}", clazz.getName(), e);
            throw new RuntimeException("字节数组转对象失败", e);
        }
    }
    
    /**
     * 格式化JSON字符串
     * @param json JSON字符串
     * @return 格式化后的字符串
     */
    public static String prettyPrint(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            Object obj = OBJECT_MAPPER.readValue(json, Object.class);
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("格式化JSON失败: {}", json, e);
            return json;
        }
    }
}
