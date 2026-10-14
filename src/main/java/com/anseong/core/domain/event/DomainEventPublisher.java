package com.anseong.core.domain.event;

public interface DomainEventPublisher {
     void publish(DomainEvent event);
}
