package com.aiassistant.common.mq;

/**
 * 消息队列接口
 * 定义消息发送和订阅的标准操作
 */
public interface MessageQueue {
    
    /**
     * 发送消息到指定主题
     * @param topic 主题名称
     * @param message 消息内容
     */
    void send(String topic, Object message);
    
    /**
     * 订阅指定主题的消息
     * @param topic 主题名称
     * @param handler 消息处理器
     */
    void subscribe(String topic, java.util.function.Consumer<String> handler);
    
    /**
     * 取消订阅指定主题
     * @param topic 主题名称
     */
    default void unsubscribe(String topic) {
        // 默认空实现
    }
}
