package com.aiassistant.core.workflow.model;

/**
 * Represents the state of a workflow execution.
 */
public enum WorkflowState {
    
    /**
     * The workflow has been created but not started.
     */
    CREATED,
    
    /**
     * The workflow is currently running.
     */
    RUNNING,
    
    /**
     * The workflow is paused.
     */
    PAUSED,
    
    /**
     * The workflow has completed successfully.
     */
    COMPLETED,
    
    /**
     * The workflow has failed.
     */
    FAILED
}
