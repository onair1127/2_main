package com.anseong.core.application.queries.sns.services;

import java.util.Collection;
import java.util.List;

import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.user.UserId;

public interface SnsQueryRepositoryService {
     List<SnsResponse> findByUserIds(Collection<UserId> userIds);
}
