package com.anseong.core.application.queries.user.services;

import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.response.UserResponse;

public interface UserQueryRepositoryService {
     UserResponse findByUsername(String username);

     UserResponse findByNickname(String nickname);

     String findMerchantUidById(UserId userId);

     String findCustomerUidById(UserId userId);

     String findUsernameByPhone(String phone);
}
