package com.aiassistant.core.embedding.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Component for splitting text into chunks for embedding.
 */
@Component
@Slf4j
public class TextSplitter {
    
    /**
     * The target size of each chunk in characters.
     */
    private int chunkSize = 512;
    
    /**
     * The overlap between consecutive chunks in characters.
     */
    private int chunkOverlap = 50;
    
    public TextSplitter() {
    }
    
    public TextSplitter(int chunkSize, int chunkOverlap) {
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
    }
    
    /**
     * Splits text into chunks with overlap.
     *
     * @param text the text to split
     * @return the list of text chunks
     */
    public List<String> split(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        
        log.debug("Splitting text of length {} into chunks of size {} with overlap {}", 
                text.length(), chunkSize, chunkOverlap);
        
        List<String> chunks = new ArrayList<>();
        
        // Split by paragraphs first
        String[] paragraphs = text.split("\\n\\n+");
        
        StringBuilder currentChunk = new StringBuilder();
        
        for (String paragraph : paragraphs) {
            // If paragraph fits in current chunk, add it
            if (currentChunk.length() + paragraph.length() + 2 <= chunkSize) {
                if (currentChunk.length() > 0) {
                    currentChunk.append("\n\n");
                }
                currentChunk.append(paragraph);
            } else {
                // If current chunk is not empty, save it and start new one with overlap
                if (currentChunk.length() > 0) {
                    chunks.add(currentChunk.toString().trim());
                    
                    // Handle overlap: take last part of current chunk
                    if (chunkOverlap > 0 && currentChunk.length() > chunkOverlap) {
                        String overlap = currentChunk.substring(currentChunk.length() - chunkOverlap);
                        currentChunk = new StringBuilder(overlap);
                    } else {
                        currentChunk = new StringBuilder();
                    }
                }
                
                // If paragraph itself is larger than chunk size, split by sentences
                if (paragraph.length() > chunkSize) {
                    splitBySentences(paragraph, chunks, currentChunk);
                } else {
                    currentChunk.append(paragraph);
                }
            }
        }
        
        // Add the last chunk if not empty
        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString().trim());
        }
        
        log.debug("Split text into {} chunks", chunks.size());
        
        return chunks;
    }
    
    /**
     * Splits a large text by sentences.
     */
    private void splitBySentences(String text, List<String> chunks, StringBuilder currentChunk) {
        // Simple sentence split by common delimiters
        String[] sentences = text.split("(?<=[.!?])\\s+");
        
        for (String sentence : sentences) {
            if (currentChunk.length() + sentence.length() + 1 <= chunkSize) {
                if (currentChunk.length() > 0) {
                    currentChunk.append(" ");
                }
                currentChunk.append(sentence);
            } else {
                // If sentence itself is too large, just split by character limit
                if (sentence.length() > chunkSize) {
                    // Save current chunk first
                    if (currentChunk.length() > 0) {
                        chunks.add(currentChunk.toString().trim());
                        currentChunk = new StringBuilder();
                    }
                    
                    // Split by character limit
                    int start = 0;
                    while (start < sentence.length()) {
                        int end = Math.min(start + chunkSize, sentence.length());
                        chunks.add(sentence.substring(start, end).trim());
                        start = end - chunkOverlap;
                        if (start < 0) start = 0;
                    }
                } else {
                    // Save current chunk and start new one
                    if (currentChunk.length() > 0) {
                        chunks.add(currentChunk.toString().trim());
                    }
                    
                    // Handle overlap
                    if (chunkOverlap > 0 && currentChunk.length() > chunkOverlap) {
                        String overlap = currentChunk.substring(currentChunk.length() - chunkOverlap);
                        currentChunk = new StringBuilder(overlap).append(" ").append(sentence);
                    } else {
                        currentChunk = new StringBuilder(sentence);
                    }
                }
            }
        }
    }
    
    public int getChunkSize() {
        return chunkSize;
    }
    
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }
    
    public int getChunkOverlap() {
        return chunkOverlap;
    }
    
    public void setChunkOverlap(int chunkOverlap) {
        this.chunkOverlap = chunkOverlap;
    }
}
