package com.anseong.core.domain.model.event.repository;

import com.anseong.core.domain.model.event.Event;

public interface EventCommandRepository {
     void save(Event event);
}
