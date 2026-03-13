package com.aiassistant.core.orchestration.stage.impl;

import com.aiassistant.core.orchestration.model.ExecutionContext;
import com.aiassistant.core.orchestration.stage.PipelineStage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Pipeline stage for checking and matching rules.
 */
@Component
@Order(20)
@Slf4j
public class RuleCheckStage implements PipelineStage {
    
    @Override
    public void execute(ExecutionContext context) throws Exception {
        log.info("Checking rules for agent: {}", context.getAgentId());
        
        // Placeholder implementation
        // In a real implementation, this would:
        // 1. Load rules for the agent
        // 2. Match rules against the user message
        // 3. Add matched rules to the context
        
        // Set empty matched rules as placeholder
        if (context.getMatchedRules() == null) {
            context.setMatchedRules(new java.util.ArrayList<>());
        }
        
        log.debug("Rule check completed for agent: {}", context.getAgentId());
    }
    
    @Override
    public String getName() {
        return "RuleCheck";
    }
}
