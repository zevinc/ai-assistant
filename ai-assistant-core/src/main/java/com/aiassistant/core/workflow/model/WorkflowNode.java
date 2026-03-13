package com.aiassistant.core.workflow.model;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a node in a workflow.
 */
@Data
public class WorkflowNode {
    
    /**
     * The unique identifier for this node.
     */
    private String id;
    
    /**
     * The name of the node.
     */
    private String name;
    
    /**
     * The type of the node (e.g., "START", "END", "LLM_CALL", "CONDITION").
     */
    private String type;
    
    /**
     * Configuration for the node.
     */
    private Map<String, Object> config;
    
    public WorkflowNode() {
        this.config = new HashMap<>();
    }
    
    public WorkflowNode(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.config = new HashMap<>();
    }
    
    /**
     * Gets a configuration value.
     */
    @SuppressWarnings("unchecked")
    public <T> T getConfig(String key) {
        return (T) config.get(key);
    }
    
    /**
     * Sets a configuration value.
     */
    public void setConfig(String key, Object value) {
        this.config.put(key, value);
    }
}
