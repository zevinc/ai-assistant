package com.aiassistant.core.orchestration.model;

import lombok.Builder;
import lombok.Data;

/**
 * Represents an orchestration request.
 */
@Data
@Builder
public class OrchestrationRequest {
    
    /**
     * The ID of the agent to use for orchestration.
     */
    private Long agentId;
    
    /**
     * The session ID for the conversation.
     */
    private String sessionId;
    
    /**
     * The user's input message.
     */
    private String userMessage;
    
    /**
     * Whether to stream the response.
     */
    private boolean stream;
    
    /**
     * Creates a simple non-streaming request.
     */
    public static OrchestrationRequest of(Long agentId, String sessionId, String userMessage) {
        return OrchestrationRequest.builder()
                .agentId(agentId)
                .sessionId(sessionId)
                .userMessage(userMessage)
                .stream(false)
                .build();
    }
    
    /**
     * Creates a streaming request.
     */
    public static OrchestrationRequest streaming(Long agentId, String sessionId, String userMessage) {
        return OrchestrationRequest.builder()
                .agentId(agentId)
                .sessionId(sessionId)
                .userMessage(userMessage)
                .stream(true)
                .build();
    }
}
