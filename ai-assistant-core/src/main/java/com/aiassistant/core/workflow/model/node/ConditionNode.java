package com.aiassistant.core.workflow.model.node;

import com.aiassistant.core.workflow.model.WorkflowNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a condition node in a workflow.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConditionNode extends WorkflowNode {
    
    /**
     * The expression to evaluate for the condition.
     */
    private String expression;
    
    public ConditionNode() {
        setType("CONDITION");
    }
    
    public ConditionNode(String id, String name) {
        super(id, name, "CONDITION");
    }
    
    public ConditionNode(String id, String name, String expression) {
        super(id, name, "CONDITION");
        this.expression = expression;
    }
}
