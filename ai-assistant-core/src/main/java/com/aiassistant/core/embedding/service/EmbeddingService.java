package com.aiassistant.core.embedding.service;

import com.aiassistant.core.embedding.model.EmbeddingResult;
import java.util.List;

/**
 * Service interface for text embedding operations.
 */
public interface EmbeddingService {
    
    /**
     * Embeds a list of texts into vectors.
     *
     * @param texts the list of texts to embed
     * @return the embedding result containing vectors and dimension
     */
    EmbeddingResult embed(List<String> texts);
    
    /**
     * Embeds a single text into a vector.
     *
     * @param text the text to embed
     * @return the embedding vector
     */
    float[] embedSingle(String text);
}
