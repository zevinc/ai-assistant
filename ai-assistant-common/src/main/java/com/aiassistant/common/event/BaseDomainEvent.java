package com.aiassistant.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 领域事件基类
 * 提供事件的通用属性和功能
 */
@Getter
public abstract class BaseDomainEvent extends ApplicationEvent implements DomainEvent {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 事件唯一标识
     */
    private final String eventId;
    
    /**
     * 事件发生时间
     */
    private final LocalDateTime occurredAt;
    
    /**
     * 聚合根ID
     */
    private final String aggregateId;
    
    /**
     * 聚合根类型
     */
    private final String aggregateType;
    
    /**
     * 构造领域事件
     * @param aggregateId 聚合根ID
     * @param aggregateType 聚合根类型
     */
    protected BaseDomainEvent(String aggregateId, String aggregateType) {
        super(aggregateId);
        this.eventId = UUID.randomUUID().toString().replace("-", "");
        this.occurredAt = LocalDateTime.now();
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
    }
    
    /**
     * 构造领域事件（带来源对象）
     * @param source 事件来源对象
     * @param aggregateId 聚合根ID
     * @param aggregateType 聚合根类型
     */
    protected BaseDomainEvent(Object source, String aggregateId, String aggregateType) {
        super(source);
        this.eventId = UUID.randomUUID().toString().replace("-", "");
        this.occurredAt = LocalDateTime.now();
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
    }
    
    @Override
    public String toString() {
        return "BaseDomainEvent{" +
                "eventId='" + eventId + '\'' +
                ", occurredAt=" + occurredAt +
                ", aggregateId='" + aggregateId + '\'' +
                ", aggregateType='" + aggregateType + '\'' +
                '}';
    }
}
