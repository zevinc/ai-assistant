package com.aiassistant.common.event;

import java.time.LocalDateTime;

/**
 * 领域事件接口
 * 所有领域事件都应实现此接口
 */
public interface DomainEvent {
    
    /**
     * 获取事件ID
     * @return 事件唯一标识
     */
    String getEventId();
    
    /**
     * 获取事件发生时间
     * @return 事件发生时间
     */
    LocalDateTime getOccurredAt();
    
    /**
     * 获取聚合根ID
     * @return 聚合根唯一标识
     */
    String getAggregateId();
    
    /**
     * 获取聚合根类型
     * @return 聚合根类型名称
     */
    String getAggregateType();
}
