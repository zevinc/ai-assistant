package com.aiassistant.common.event;

/**
 * 领域事件处理器接口
 * @param <T> 事件类型
 */
@FunctionalInterface
public interface DomainEventHandler<T extends DomainEvent> {
    
    /**
     * 处理领域事件
     * @param event 领域事件
     */
    void handle(T event);
}
