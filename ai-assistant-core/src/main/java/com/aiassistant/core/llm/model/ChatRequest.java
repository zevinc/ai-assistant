package com.aiassistant.core.llm.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * Represents a chat completion request to an LLM.
 */
@Data
@Builder
public class ChatRequest {
    
    /**
     * The list of messages in the conversation.
     */
    private List<ChatMessage> messages;
    
    /**
     * The model to use for completion.
     */
    private String model;
    
    /**
     * The temperature for controlling randomness.
     * Default: 0.7
     */
    @Builder.Default
    private Double temperature = 0.7;
    
    /**
     * The maximum number of tokens to generate.
     * Default: 2048
     */
    @Builder.Default
    private Integer maxTokens = 2048;
    
    /**
     * Whether to stream the response.
     * Default: false
     */
    @Builder.Default
    private Boolean stream = false;
    
    /**
     * Creates a simple request with a single user message.
     */
    public static ChatRequest of(String userMessage) {
        return ChatRequest.builder()
                .messages(List.of(ChatMessage.user(userMessage)))
                .build();
    }
    
    /**
     * Creates a request with system and user messages.
     */
    public static ChatRequest of(String systemMessage, String userMessage) {
        return ChatRequest.builder()
                .messages(List.of(
                        ChatMessage.system(systemMessage),
                        ChatMessage.user(userMessage)
                ))
                .build();
    }
}
