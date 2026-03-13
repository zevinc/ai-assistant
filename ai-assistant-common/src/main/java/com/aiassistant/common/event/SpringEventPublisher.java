package com.aiassistant.common.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Spring事件发布器实现
 * 基于Spring的事件机制实现领域事件发布
 */
@Slf4j
@Component
public class SpringEventPublisher implements DomainEventPublisher {
    
    private final ApplicationEventPublisher applicationEventPublisher;
    
    public SpringEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    
    @Override
    public void publish(DomainEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("发布领域事件: eventId={}, aggregateId={}, aggregateType={}",
                    event.getEventId(),
                    event.getAggregateId(),
                    event.getAggregateType());
        }
        applicationEventPublisher.publishEvent(event);
    }
}
