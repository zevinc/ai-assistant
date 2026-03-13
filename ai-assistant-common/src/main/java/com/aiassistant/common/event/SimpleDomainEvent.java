package com.aiassistant.common.event;

/**
 * 简单领域事件实现
 * 用于不需要自定义事件类的场景
 */
public class SimpleDomainEvent extends BaseDomainEvent {

    private static final long serialVersionUID = 1L;

    private final String eventName;

    public SimpleDomainEvent(String aggregateId, String aggregateType) {
        super(aggregateId, aggregateType);
        this.eventName = aggregateType;
    }

    public SimpleDomainEvent(String aggregateId, String aggregateType, String eventName) {
        super(aggregateId, aggregateType);
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }
}
