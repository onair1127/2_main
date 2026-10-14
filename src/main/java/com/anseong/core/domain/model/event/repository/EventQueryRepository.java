package com.anseong.core.domain.model.event.repository;

import com.anseong.core.domain.model.user.UserId;

public interface EventQueryRepository {
     boolean existsByUserId(UserId userId);
}
