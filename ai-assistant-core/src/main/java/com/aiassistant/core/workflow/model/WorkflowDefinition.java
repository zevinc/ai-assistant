package com.aiassistant.core.workflow.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a workflow definition with nodes and edges.
 */
@Data
public class WorkflowDefinition {
    
    /**
     * The unique identifier for this workflow.
     */
    private String id;
    
    /**
     * The name of the workflow.
     */
    private String name;
    
    /**
     * The description of the workflow.
     */
    private String description;
    
    /**
     * The list of nodes in the workflow.
     */
    private List<WorkflowNode> nodes;
    
    /**
     * The list of edges connecting nodes.
     */
    private List<WorkflowEdge> edges;
    
    public WorkflowDefinition() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }
    
    /**
     * Represents an edge connecting two nodes in a workflow.
     */
    @Data
    public static class WorkflowEdge {
        /**
         * The source node ID.
         */
        private String sourceId;
        
        /**
         * The target node ID.
         */
        private String targetId;
        
        /**
         * Optional condition for the edge.
         */
        private String condition;
        
        public WorkflowEdge() {
        }
        
        public WorkflowEdge(String sourceId, String targetId) {
            this.sourceId = sourceId;
            this.targetId = targetId;
        }
        
        public WorkflowEdge(String sourceId, String targetId, String condition) {
            this.sourceId = sourceId;
            this.targetId = targetId;
            this.condition = condition;
        }
    }
}
