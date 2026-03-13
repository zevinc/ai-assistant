package com.aiassistant.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j OpenAPI 配置类
 * 基于 SpringDoc OpenAPI 3.0
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public GroupedOpenApi aiAssistantApi() {
        return GroupedOpenApi.builder()
                .group("ai-assistant")
                .packagesToScan("com.aiassistant")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI Assistant API")
                        .version("1.0.0")
                        .description("AI Assistant 智能助手系统 RESTful API 文档"));
    }
}
