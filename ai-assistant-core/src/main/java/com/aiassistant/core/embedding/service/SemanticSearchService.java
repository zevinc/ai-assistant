package com.aiassistant.core.embedding.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for semantic search operations.
 */
@Service
@Slf4j
public class SemanticSearchService {
    
    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;
    
    public SemanticSearchService(EmbeddingService embeddingService, VectorStore vectorStore) {
        this.embeddingService = embeddingService;
        this.vectorStore = vectorStore;
    }
    
    /**
     * Performs a semantic search for the given query.
     *
     * @param query the search query
     * @param topK the maximum number of results to return
     * @return the list of search results ordered by relevance
     */
    public List<VectorStore.VectorSearchResult> search(String query, int topK) {
        log.debug("Performing semantic search for query: {} with topK: {}", query, topK);
        
        // Embed the query
        float[] queryVector = embeddingService.embedSingle(query);
        
        // Search the vector store
        List<VectorStore.VectorSearchResult> results = vectorStore.search(queryVector, topK);
        
        log.debug("Found {} results for query", results.size());
        
        return results;
    }
}
