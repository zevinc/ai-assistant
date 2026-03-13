package com.aiassistant.core.orchestration.model;

import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the execution context for the orchestration pipeline.
 */
@Data
@Builder
public class ExecutionContext {
    
    /**
     * The ID of the agent being executed.
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
     * The retrieved memories for context.
     */
    private List<String> memories;
    
    /**
     * The matched rules for the agent.
     */
    private List<String> matchedRules;
    
    /**
     * The results from skill executions.
     */
    private Map<String, Object> skillResults;
    
    /**
     * The generated response.
     */
    private String response;
    
    /**
     * Additional metadata for the execution.
     */
    private Map<String, Object> metadata;
    
    /**
     * Default constructor that initializes empty collections.
     */
    public ExecutionContext() {
        this.memories = new ArrayList<>();
        this.matchedRules = new ArrayList<>();
        this.skillResults = new HashMap<>();
        this.metadata = new HashMap<>();
    }
    
    /**
     * Builder constructor that ensures collections are never null.
     */
    public ExecutionContext(Long agentId, String sessionId, String userMessage,
                           List<String> memories, List<String> matchedRules,
                           Map<String, Object> skillResults, String response,
                           Map<String, Object> metadata) {
        this.agentId = agentId;
        this.sessionId = sessionId;
        this.userMessage = userMessage;
        this.memories = memories != null ? memories : new ArrayList<>();
        this.matchedRules = matchedRules != null ? matchedRules : new ArrayList<>();
        this.skillResults = skillResults != null ? skillResults : new HashMap<>();
        this.response = response;
        this.metadata = metadata != null ? metadata : new HashMap<>();
    }
    
    /**
     * Adds a memory to the context.
     */
    public void addMemory(String memory) {
        if (this.memories == null) {
            this.memories = new ArrayList<>();
        }
        this.memories.add(memory);
    }
    
    /**
     * Adds a matched rule to the context.
     */
    public void addMatchedRule(String rule) {
        if (this.matchedRules == null) {
            this.matchedRules = new ArrayList<>();
        }
        this.matchedRules.add(rule);
    }
    
    /**
     * Adds a skill result to the context.
     */
    public void addSkillResult(String skillName, Object result) {
        if (this.skillResults == null) {
            this.skillResults = new HashMap<>();
        }
        this.skillResults.put(skillName, result);
    }
    
    /**
     * Adds metadata to the context.
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }
}
