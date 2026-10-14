package com.anseong.core.domain.event;

import org.springframework.context.ApplicationEvent;

public abstract class DomainEvent extends ApplicationEvent {

	private static final long serialVersionUID = -5205114519198341694L;

     public DomainEvent(Object source) {
          super(source);
     }

     public long occuredAt() {
          return getTimestamp();
     }
}
