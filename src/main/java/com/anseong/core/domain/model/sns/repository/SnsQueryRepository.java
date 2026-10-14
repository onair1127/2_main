package com.anseong.core.domain.model.sns.repository;

import java.util.Collection;
import java.util.List;

import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.user.UserId;

public interface SnsQueryRepository {
     List<SnsResponse> findByUserId(UserId userId);

     List<SnsResponse> findByUserIds(Collection<UserId> userIds);
}
