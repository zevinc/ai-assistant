package com.aiassistant.common.web;

import com.aiassistant.common.annotation.ApiVersion;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * API版本处理器映射
 * 自动为带有@ApiVersion注解的Controller添加版本前缀
 */
public class ApiVersionHandlerMapping extends RequestMappingHandlerMapping {
    
    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
        if (info == null) {
            return null;
        }
        
        // 检查方法级别的@ApiVersion注解
        ApiVersion methodAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, ApiVersion.class);
        if (methodAnnotation != null) {
            return createVersionInfo(info, methodAnnotation.value());
        }
        
        // 检查类级别的@ApiVersion注解
        ApiVersion classAnnotation = AnnotatedElementUtils.findMergedAnnotation(handlerType, ApiVersion.class);
        if (classAnnotation != null) {
            return createVersionInfo(info, classAnnotation.value());
        }
        
        return info;
    }
    
    /**
     * 创建带版本前缀的RequestMappingInfo
     * @param info 原始映射信息
     * @param version 版本号
     * @return 新的映射信息
     */
    private RequestMappingInfo createVersionInfo(RequestMappingInfo info, int version) {
        // 构建版本前缀路径
        String versionPattern = "/api/v" + version;
        
        // 创建新的路径条件
        PatternsRequestCondition patternsCondition = info.getPatternsCondition();
        if (patternsCondition == null) {
            return info;
        }
        
        // 为所有路径添加版本前缀
        String[] newPatterns = patternsCondition.getPatterns().stream()
                .map(pattern -> versionPattern + pattern)
                .toArray(String[]::new);
        
        PatternsRequestCondition newPatternsCondition = 
                new PatternsRequestCondition(newPatterns, null, null, false, true);
        
        // 返回新的RequestMappingInfo
        return new RequestMappingInfo(
                null,
                newPatternsCondition,
                info.getMethodsCondition(),
                info.getParamsCondition(),
                info.getHeadersCondition(),
                info.getConsumesCondition(),
                info.getProducesCondition(),
                info.getCustomCondition()
        );
    }
}
