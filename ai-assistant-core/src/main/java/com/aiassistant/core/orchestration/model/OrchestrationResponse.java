package com.aiassistant.core.orchestration.model;

import lombok.Builder;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an orchestration response.
 */
@Data
@Builder
public class OrchestrationResponse {
    
    /**
     * The generated content.
     */
    private String content;
    
    /**
     * The session ID for the conversation.
     */
    private String sessionId;
    
    /**
     * The total tokens used in the request.
     */
    private int tokensUsed;
    
    /**
     * Additional metadata for the response.
     */
    private Map<String, Object> metadata;
    
    /**
     * Creates a simple response with content.
     */
    public static OrchestrationResponse of(String content, String sessionId) {
        return OrchestrationResponse.builder()
                .content(content)
                .sessionId(sessionId)
                .build();
    }
    
    /**
     * Creates an error response.
     */
    public static OrchestrationResponse error(String errorMessage, String sessionId) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("error", true);
        return OrchestrationResponse.builder()
                .content(errorMessage)
                .sessionId(sessionId)
                .metadata(metadata)
                .build();
    }
}
