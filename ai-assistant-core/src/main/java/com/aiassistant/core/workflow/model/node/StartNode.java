package com.aiassistant.core.workflow.model.node;

import com.aiassistant.core.workflow.model.WorkflowNode;

/**
 * Represents a start node in a workflow.
 */
public class StartNode extends WorkflowNode {
    
    public StartNode() {
        setType("START");
    }
    
    public StartNode(String id, String name) {
        super(id, name, "START");
    }
}
