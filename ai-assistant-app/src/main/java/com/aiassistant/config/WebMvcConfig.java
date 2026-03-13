package com.aiassistant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置类
 * 配置 CORS 和路径匹配
 *
 * @author AI Assistant
 * @version 1.0.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置 CORS 跨域映射
     * 允许所有来源访问
     *
     * @param registry CORS 注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许所有来源
                .allowedOriginPatterns("*")
                // 允许的 HTTP 方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD")
                // 允许的请求头
                .allowedHeaders("*")
                // 允许携带凭证
                .allowCredentials(true)
                // 预检请求缓存时间（秒）
                .maxAge(3600);
    }

    /**
     * 配置路径匹配
     * 启用后缀模式匹配（可选）
     *
     * @param configurer 路径匹配配置器
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 启用 URL 后缀模式匹配，如 /api/user.json 或 /api/user.xml
        configurer.setUseSuffixPatternMatch(true);
        // 启用尾部斜杠匹配，如 /api/user/ 等同于 /api/user
        configurer.setUseTrailingSlashMatch(true);
    }
}
