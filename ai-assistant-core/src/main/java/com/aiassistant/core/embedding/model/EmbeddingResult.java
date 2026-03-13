package com.aiassistant.core.embedding.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * Represents the result of an embedding operation.
 */
@Data
@Builder
public class EmbeddingResult {
    
    /**
     * The list of embedding vectors.
     */
    private List<float[]> vectors;
    
    /**
     * The dimension of each embedding vector.
     */
    private int dimension;
    
    /**
     * Creates an embedding result with vectors and inferred dimension.
     */
    public static EmbeddingResult of(List<float[]> vectors) {
        int dimension = vectors.isEmpty() ? 0 : vectors.get(0).length;
        return EmbeddingResult.builder()
                .vectors(vectors)
                .dimension(dimension)
                .build();
    }
    
    /**
     * Creates an embedding result for a single vector.
     */
    public static EmbeddingResult ofSingle(float[] vector) {
        return EmbeddingResult.builder()
                .vectors(List.of(vector))
                .dimension(vector.length)
                .build();
    }
}
