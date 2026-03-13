package com.aiassistant.common.annotation;

import java.lang.annotation.*;

/**
 * API版本注解
 * 用于标注Controller或方法的API版本号
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {
    
    /**
     * API版本号
     * @return 版本号，默认为1
     */
    int value() default 1;
}
