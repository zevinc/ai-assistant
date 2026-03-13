package com.aiassistant.core.embedding.service;

import java.util.List;
import java.util.Map;

/**
 * Interface for vector storage operations.
 */
public interface VectorStore {
    
    /**
     * Saves a vector with associated metadata.
     *
     * @param id the unique identifier for the vector
     * @param vector the embedding vector
     * @param metadata the associated metadata
     */
    void save(String id, float[] vector, Map<String, Object> metadata);
    
    /**
     * Searches for similar vectors.
     *
     * @param queryVector the query vector
     * @param topK the maximum number of results to return
     * @return the list of search results ordered by similarity
     */
    List<VectorSearchResult> search(float[] queryVector, int topK);
    
    /**
     * Deletes a vector by ID.
     *
     * @param id the unique identifier of the vector to delete
     */
    void delete(String id);
    
    /**
     * Represents a vector search result.
     */
    record VectorSearchResult(
            String id,
            float score,
            Map<String, Object> metadata
    ) {}
}
