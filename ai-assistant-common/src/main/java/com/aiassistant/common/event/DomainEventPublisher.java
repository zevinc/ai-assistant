package com.aiassistant.common.event;

/**
 * 领域事件发布器接口
 * 用于发布领域事件
 */
public interface DomainEventPublisher {
    
    /**
     * 发布领域事件
     * @param event 领域事件
     */
    void publish(DomainEvent event);
}
