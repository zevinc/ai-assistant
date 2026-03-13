package com.aiassistant.core.embedding.service.impl;

import com.aiassistant.core.embedding.model.EmbeddingResult;
import com.aiassistant.core.embedding.service.EmbeddingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of EmbeddingService using Spring AI.
 */
@Service
@Slf4j
public class EmbeddingServiceImpl implements EmbeddingService {
    
    private final EmbeddingModel embeddingModel;
    
    @Autowired(required = false)
    public EmbeddingServiceImpl(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
        if (embeddingModel == null) {
            log.warn("EmbeddingModel not configured. Embedding service will throw exceptions when called.");
        }
    }
    
    @Override
    public EmbeddingResult embed(List<String> texts) {
        if (embeddingModel == null) {
            throw new IllegalStateException("EmbeddingModel is not configured. Please configure a Spring AI EmbeddingModel.");
        }
        
        log.debug("Embedding {} texts", texts.size());
        
        try {
            EmbeddingResponse response = embeddingModel.embedForResponse(texts);
            
            List<float[]> vectors = new ArrayList<>();
            int dimension = 0;
            
            for (org.springframework.ai.embedding.Embedding embedding : response.getResults()) {
                float[] vector = embedding.getOutput();
                vectors.add(vector);
                if (dimension == 0 && vector != null) {
                    dimension = vector.length;
                }
            }
            
            log.debug("Generated {} embedding vectors with dimension {}", vectors.size(), dimension);
            
            return EmbeddingResult.builder()
                    .vectors(vectors)
                    .dimension(dimension)
                    .build();
                    
        } catch (Exception e) {
            log.error("Error during embedding: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to embed texts", e);
        }
    }
    
    @Override
    public float[] embedSingle(String text) {
        log.debug("Embedding single text");
        
        EmbeddingResult result = embed(List.of(text));
        
        if (result.getVectors().isEmpty()) {
            throw new RuntimeException("Failed to embed text: no vector returned");
        }
        
        return result.getVectors().get(0);
    }
}
