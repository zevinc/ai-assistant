package com.aiassistant.core.llm.service.impl;

import com.aiassistant.core.llm.model.ChatMessage;
import com.aiassistant.core.llm.model.ChatRequest;
import com.aiassistant.core.llm.model.ChatResponse;
import com.aiassistant.core.llm.service.LlmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of LlmService using Spring AI.
 */
@Service
@Slf4j
public class LlmServiceImpl implements LlmService {
    
    private final ChatModel chatModel;
    
    @Autowired(required = false)
    public LlmServiceImpl(ChatModel chatModel) {
        this.chatModel = chatModel;
        if (chatModel == null) {
            log.warn("ChatModel not configured. LLM service will throw exceptions when called.");
        }
    }
    
    @Override
    public ChatResponse chat(ChatRequest request) {
        if (chatModel == null) {
            throw new IllegalStateException("ChatModel is not configured. Please configure a Spring AI ChatModel.");
        }
        
        log.debug("Processing chat request with {} messages", request.getMessages().size());
        
        try {
            // Convert ChatRequest to Spring AI Prompt
            List<Message> messages = convertMessages(request.getMessages());
            Prompt prompt = new Prompt(messages);
            
            // Call the chat model
            org.springframework.ai.chat.model.ChatResponse springResponse = chatModel.call(prompt);
            
            // Convert response - use getText() for AssistantMessage content
            AssistantMessage output = springResponse.getResult().getOutput();
            String content = output.getText();
            
            String finishReason = springResponse.getResult().getMetadata() != null 
                    ? springResponse.getResult().getMetadata().getFinishReason() 
                    : null;
            
            Integer promptTokens = null;
            Integer completionTokens = null;
            Integer totalTokens = null;
            
            if (springResponse.getMetadata() != null && springResponse.getMetadata().getUsage() != null) {
                var usage = springResponse.getMetadata().getUsage();
                promptTokens = usage.getPromptTokens() != null ? usage.getPromptTokens().intValue() : null;
                // Use getGenerationTokens() for completion tokens (Spring AI naming)
                try {
                    var genTokensMethod = usage.getClass().getMethod("getGenerationTokens");
                    var genTokens = genTokensMethod.invoke(usage);
                    if (genTokens instanceof Long) {
                        completionTokens = ((Long) genTokens).intValue();
                    }
                } catch (NoSuchMethodException e) {
                    // Try alternative method name
                    try {
                        var outputTokensMethod = usage.getClass().getMethod("getOutputTokens");
                        var outputTokens = outputTokensMethod.invoke(usage);
                        if (outputTokens instanceof Long) {
                            completionTokens = ((Long) outputTokens).intValue();
                        }
                    } catch (Exception ignored) {}
                } catch (Exception ignored) {}
                totalTokens = usage.getTotalTokens() != null ? usage.getTotalTokens().intValue() : null;
            }
            
            ChatResponse response = ChatResponse.builder()
                    .content(content)
                    .finishReason(finishReason)
                    .promptTokens(promptTokens)
                    .completionTokens(completionTokens)
                    .totalTokens(totalTokens)
                    .build();
            
            log.debug("Chat response generated. Total tokens: {}", totalTokens);
            
            return response;
            
        } catch (Exception e) {
            log.error("Error during chat completion: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to complete chat request", e);
        }
    }
    
    @Override
    public Flux<String> chatStream(ChatRequest request) {
        if (chatModel == null) {
            return Flux.error(new IllegalStateException("ChatModel is not configured. Please configure a Spring AI ChatModel."));
        }
        
        log.debug("Processing streaming chat request with {} messages", request.getMessages().size());
        
        try {
            // Convert ChatRequest to Spring AI Prompt
            List<Message> messages = convertMessages(request.getMessages());
            Prompt prompt = new Prompt(messages);
            
            // Stream from the chat model
            return chatModel.stream(prompt)
                    .<String>handle((chunk, sink) -> {
                        if (chunk.getResult() != null 
                                && chunk.getResult().getOutput() != null) {
                            String text = chunk.getResult().getOutput().getText();
                            if (text != null && !text.isEmpty()) {
                                sink.next(text);
                            }
                        }
                    })
                    .doOnComplete(() -> log.debug("Streaming chat completed"))
                    .doOnError(e -> log.error("Error during streaming chat: {}", e.getMessage(), e));
                    
        } catch (Exception e) {
            log.error("Error initiating streaming chat: {}", e.getMessage(), e);
            return Flux.error(new RuntimeException("Failed to initiate streaming chat", e));
        }
    }
    
    /**
     * Converts ChatMessage list to Spring AI Message list.
     */
    private List<Message> convertMessages(List<ChatMessage> chatMessages) {
        List<Message> messages = new ArrayList<>();
        
        for (ChatMessage chatMessage : chatMessages) {
            Message message = switch (chatMessage.getRole().toLowerCase()) {
                case "system" -> new SystemMessage(chatMessage.getContent());
                case "user" -> new UserMessage(chatMessage.getContent());
                case "assistant" -> new AssistantMessage(chatMessage.getContent());
                default -> throw new IllegalArgumentException("Unknown message role: " + chatMessage.getRole());
            };
            messages.add(message);
        }
        
        return messages;
    }
}
