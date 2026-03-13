package com.aiassistant.core.llm.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for LLM services.
 */
@Configuration
@Slf4j
public class LlmConfig {
    
    @PostConstruct
    public void init() {
        log.info("LLM service initialized");
    }
}
