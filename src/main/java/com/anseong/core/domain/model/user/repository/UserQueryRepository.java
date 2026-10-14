package com.anseong.core.domain.model.user.repository;

import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.response.UserResponse;

public interface UserQueryRepository {
     UserResponse findByUsername(String username);

	UserResponse findByNickname(String nickname);

	UserResponse findByUserId(UserId userId);

     String findMerchantUidById(UserId userId);

     String findCustomerUidById(UserId userId);

     boolean existsByPhone(String phone);

     boolean existsByNickname(String nickname);

     UserId findIdByPhone(String phone);

     String findUsernameByPhone(String phone);
}
