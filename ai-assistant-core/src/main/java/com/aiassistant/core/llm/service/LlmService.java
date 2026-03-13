package com.aiassistant.core.llm.service;

import com.aiassistant.core.llm.model.ChatRequest;
import com.aiassistant.core.llm.model.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * Service interface for LLM chat operations.
 */
public interface LlmService {
    
    /**
     * Performs a synchronous chat completion.
     *
     * @param request the chat request containing messages and parameters
     * @return the chat response with generated content
     */
    ChatResponse chat(ChatRequest request);
    
    /**
     * Performs a streaming chat completion.
     *
     * @param request the chat request containing messages and parameters
     * @return a flux of content chunks as they are generated
     */
    Flux<String> chatStream(ChatRequest request);
}
