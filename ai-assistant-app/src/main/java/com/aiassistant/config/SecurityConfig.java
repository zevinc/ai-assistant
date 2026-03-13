package com.aiassistant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security 配置类
 * 开发模式下允许所有请求通过
 *
 * @author AI Assistant
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 配置安全过滤链
     * 开发模式下允许所有请求，禁用 CSRF，启用 CORS
     *
     * @param http HttpSecurity 配置
     * @return SecurityFilterChain 安全过滤链
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF（开发模式）
                .csrf(csrf -> csrf.disable())
                // 启用 CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 允许所有请求（开发模式）
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    /**
     * 配置 CORS
     * 允许所有来源、方法和头部
     *
     * @return CorsConfigurationSource CORS 配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 允许所有来源
        configuration.setAllowedOriginPatterns(List.of("*"));
        
        // 允许所有 HTTP 方法
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
        ));
        
        // 允许所有请求头
        configuration.setAllowedHeaders(List.of("*"));
        
        // 允许携带凭证
        configuration.setAllowCredentials(true);
        
        // 预检请求缓存时间（秒）
        configuration.setMaxAge(3600L);
        
        // 暴露响应头
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization", "Content-Disposition", "X-Request-Id"
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    /**
     * Web 安全自定义配置
     * 忽略静态资源和文档路径
     *
     * @return WebSecurityCustomizer 自定义配置
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/doc.html",
                        "/webjars/**",
                        "/favicon.ico",
                        "/static/**"
                );
    }
}
