package com.anseong.core.domain.model.sns.repository;

import java.util.List;

import com.anseong.core.domain.model.sns.Sns;
import com.anseong.core.domain.model.user.UserId;

public interface SnsCommandRepository {

     void save(Sns sns);

	void save(List<Sns> sns);

	void remove(Sns sns);

     void removeByUserId(UserId userId);

     void removeByUserIdAndType(UserId userId, String type);
}
