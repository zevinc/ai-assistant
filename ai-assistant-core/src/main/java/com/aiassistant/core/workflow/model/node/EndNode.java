package com.aiassistant.core.workflow.model.node;

import com.aiassistant.core.workflow.model.WorkflowNode;

/**
 * Represents an end node in a workflow.
 */
public class EndNode extends WorkflowNode {
    
    public EndNode() {
        setType("END");
    }
    
    public EndNode(String id, String name) {
        super(id, name, "END");
    }
}
