package com.aiassistant.common.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson配置类
 */
@Configuration
public class JacksonConfig {
    
    /**
     * 配置Jackson ObjectMapper
     * 注册Java 8时间模块，配置日期格式
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 注册Java 8时间模块
            builder.modules(new JavaTimeModule());
            
            // 配置日期时间格式
            builder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            // 禁用日期时间作为时间戳
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }
}
