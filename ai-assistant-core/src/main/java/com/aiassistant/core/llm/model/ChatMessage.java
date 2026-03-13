package com.aiassistant.core.llm.model;

import lombok.Data;

/**
 * Represents a single message in a chat conversation.
 */
@Data
public class ChatMessage {
    
    /**
     * The role of the message sender.
     * Values: "system", "user", "assistant"
     */
    private String role;
    
    /**
     * The content of the message.
     */
    private String content;
    
    /**
     * Optional name of the message sender.
     */
    private String name;
    
    public ChatMessage() {
    }
    
    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
    
    public ChatMessage(String role, String content, String name) {
        this.role = role;
        this.content = content;
        this.name = name;
    }
    
    /**
     * Creates a system message.
     */
    public static ChatMessage system(String content) {
        return new ChatMessage("system", content);
    }
    
    /**
     * Creates a user message.
     */
    public static ChatMessage user(String content) {
        return new ChatMessage("user", content);
    }
    
    /**
     * Creates an assistant message.
     */
    public static ChatMessage assistant(String content) {
        return new ChatMessage("assistant", content);
    }
}
