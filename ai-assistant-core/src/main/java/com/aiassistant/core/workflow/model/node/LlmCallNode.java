package com.aiassistant.core.workflow.model.node;

import com.aiassistant.core.workflow.model.WorkflowNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents an LLM call node in a workflow.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LlmCallNode extends WorkflowNode {
    
    /**
     * The prompt template for the LLM call.
     */
    private String prompt;
    
    public LlmCallNode() {
        setType("LLM_CALL");
    }
    
    public LlmCallNode(String id, String name) {
        super(id, name, "LLM_CALL");
    }
    
    public LlmCallNode(String id, String name, String prompt) {
        super(id, name, "LLM_CALL");
        this.prompt = prompt;
    }
}
