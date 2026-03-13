package com.aiassistant.core.orchestration.service;

import com.aiassistant.core.orchestration.model.ExecutionContext;
import com.aiassistant.core.orchestration.model.OrchestrationRequest;
import com.aiassistant.core.orchestration.model.OrchestrationResponse;
import com.aiassistant.core.orchestration.stage.PipelineStage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Main orchestration engine that executes pipeline stages.
 */
@Service
@Slf4j
public class OrchestrationEngine {
    
    private final List<PipelineStage> stages;
    
    public OrchestrationEngine(List<PipelineStage> stages) {
        // Sort stages by @Order annotation
        this.stages = stages.stream()
                .sorted(Comparator.comparingInt(stage -> {
                    org.springframework.core.annotation.Order order = 
                            stage.getClass().getAnnotation(org.springframework.core.annotation.Order.class);
                    return order != null ? order.value() : Integer.MAX_VALUE;
                }))
                .toList();
        
        log.info("OrchestrationEngine initialized with {} stages", this.stages.size());
        this.stages.forEach(stage -> {
            org.springframework.core.annotation.Order order = 
                    stage.getClass().getAnnotation(org.springframework.core.annotation.Order.class);
            log.debug("  - Stage: {} (order: {})", stage.getName(), order != null ? order.value() : "none");
        });
    }
    
    /**
     * Executes the orchestration pipeline for the given request.
     *
     * @param request the orchestration request
     * @return the orchestration response
     */
    public OrchestrationResponse execute(OrchestrationRequest request) {
        log.info("Starting orchestration for agent: {}, session: {}", 
                request.getAgentId(), request.getSessionId());
        
        // Build execution context from request
        ExecutionContext context = ExecutionContext.builder()
                .agentId(request.getAgentId())
                .sessionId(request.getSessionId())
                .userMessage(request.getUserMessage())
                .build();
        
        try {
            // Execute each stage in order
            for (PipelineStage stage : stages) {
                log.debug("Executing stage: {}", stage.getName());
                
                long startTime = System.currentTimeMillis();
                stage.execute(context);
                long duration = System.currentTimeMillis() - startTime;
                
                log.debug("Stage {} completed in {}ms", stage.getName(), duration);
            }
            
            // Build response from context
            OrchestrationResponse response = OrchestrationResponse.builder()
                    .content(context.getResponse())
                    .sessionId(context.getSessionId())
                    .metadata(context.getMetadata())
                    .build();
            
            log.info("Orchestration completed successfully for session: {}", request.getSessionId());
            
            return response;
            
        } catch (Exception e) {
            log.error("Error during orchestration for session {}: {}", 
                    request.getSessionId(), e.getMessage(), e);
            
            // Build error response
            return OrchestrationResponse.error(
                    "An error occurred during processing: " + e.getMessage(),
                    request.getSessionId()
            );
        }
    }
}
