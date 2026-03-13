package com.aiassistant.core.llm.model;

import lombok.Data;

/**
 * Configuration for an LLM provider.
 */
@Data
public class ModelConfig {
    
    /**
     * The provider name (e.g., "openai", "anthropic", "azure").
     */
    private String provider;
    
    /**
     * The model name (e.g., "gpt-4", "claude-3-opus").
     */
    private String modelName;
    
    /**
     * The API key for authentication.
     */
    private String apiKey;
    
    /**
     * The base URL for the API endpoint.
     */
    private String baseUrl;
    
    public ModelConfig() {
    }
    
    public ModelConfig(String provider, String modelName, String apiKey, String baseUrl) {
        this.provider = provider;
        this.modelName = modelName;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }
}
