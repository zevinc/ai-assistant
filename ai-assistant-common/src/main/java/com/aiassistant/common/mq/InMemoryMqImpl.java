package com.aiassistant.common.mq;

import com.aiassistant.common.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * 内存消息队列实现
 * 用于开发测试环境或单机部署场景
 */
@Slf4j
@Component
@ConditionalOnMissingBean(MessageQueue.class)
public class InMemoryMqImpl implements MessageQueue {
    
    /**
     * 主题订阅者映射
     */
    private final Map<String, List<Consumer<String>>> subscribers = new ConcurrentHashMap<>();
    
    @Override
    public void send(String topic, Object message) {
        log.debug("发送消息到主题: topic={}, message={}", topic, message);
        
        // 序列化消息为JSON
        String jsonMessage;
        if (message instanceof String) {
            jsonMessage = (String) message;
        } else {
            jsonMessage = JacksonUtils.toJson(message);
        }
        
        // 获取该主题的所有订阅者
        List<Consumer<String>> handlers = subscribers.get(topic);
        if (handlers == null || handlers.isEmpty()) {
            log.warn("主题没有订阅者: topic={}", topic);
            return;
        }
        
        // 通知所有订阅者
        for (Consumer<String> handler : handlers) {
            try {
                handler.accept(jsonMessage);
            } catch (Exception e) {
                log.error("消息处理失败: topic={}, message={}", topic, jsonMessage, e);
            }
        }
    }
    
    @Override
    public void subscribe(String topic, Consumer<String> handler) {
        log.info("订阅主题: topic={}", topic);
        subscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(handler);
    }
    
    @Override
    public void unsubscribe(String topic) {
        log.info("取消订阅主题: topic={}", topic);
        subscribers.remove(topic);
    }
    
    /**
     * 获取主题订阅者数量
     * @param topic 主题名称
     * @return 订阅者数量
     */
    public int getSubscriberCount(String topic) {
        List<Consumer<String>> handlers = subscribers.get(topic);
        return handlers != null ? handlers.size() : 0;
    }
    
    /**
     * 获取所有主题
     * @return 主题集合
     */
    public java.util.Set<String> getTopics() {
        return subscribers.keySet();
    }
}
