package com.aiassistant.core.embedding.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * Represents an embedding request for text vectorization.
 */
@Data
@Builder
public class EmbeddingRequest {
    
    /**
     * The list of texts to embed.
     */
    private List<String> texts;
    
    /**
     * Creates an embedding request from a list of texts.
     */
    public static EmbeddingRequest of(List<String> texts) {
        return EmbeddingRequest.builder()
                .texts(texts)
                .build();
    }
    
    /**
     * Creates an embedding request from a single text.
     */
    public static EmbeddingRequest of(String text) {
        return EmbeddingRequest.builder()
                .texts(List.of(text))
                .build();
    }
}
