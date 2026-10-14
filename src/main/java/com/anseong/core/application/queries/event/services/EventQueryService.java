package com.anseong.core.application.queries.event.services;

import com.anseong.core.domain.model.user.UserId;

public interface EventQueryService {
     public Long checkUserEventCount(UserId userId);
}
