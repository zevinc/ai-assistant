package com.aiassistant.core.llm.model;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a chat completion response from an LLM.
 */
@Data
@Builder
public class ChatResponse {
    
    /**
     * The generated content.
     */
    private String content;
    
    /**
     * The reason for completion.
     * Values: "stop", "length", "content_filter", etc.
     */
    private String finishReason;
    
    /**
     * The number of tokens in the prompt.
     */
    private Integer promptTokens;
    
    /**
     * The number of tokens in the completion.
     */
    private Integer completionTokens;
    
    /**
     * The total number of tokens used.
     */
    private Integer totalTokens;
    
    /**
     * Creates a simple response with just content.
     */
    public static ChatResponse of(String content) {
        return ChatResponse.builder()
                .content(content)
                .build();
    }
    
    /**
     * Creates a response with content and token usage.
     */
    public static ChatResponse of(String content, int promptTokens, int completionTokens) {
        return ChatResponse.builder()
                .content(content)
                .promptTokens(promptTokens)
                .completionTokens(completionTokens)
                .totalTokens(promptTokens + completionTokens)
                .build();
    }
}
