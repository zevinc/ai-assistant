package com.aiassistant.core.orchestration.stage;

import com.aiassistant.core.orchestration.model.ExecutionContext;

/**
 * Interface for pipeline stages in the orchestration engine.
 */
public interface PipelineStage {
    
    /**
     * Executes this stage in the pipeline.
     *
     * @param context the execution context to process
     * @throws Exception if an error occurs during execution
     */
    void execute(ExecutionContext context) throws Exception;
    
    /**
     * Returns the name of this pipeline stage.
     *
     * @return the stage name
     */
    String getName();
}
