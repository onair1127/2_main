package com.anseong.core.application.queries.user.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.queries.user.services.UserQueryRepositoryService;
import com.anseong.core.domain.aop.semaphore.ReplicaDatasourceSemaphore;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.repository.UserQueryRepository;
import com.anseong.core.domain.model.user.response.UserResponse;

@Service
@Transactional(readOnly = true)
@ReplicaDatasourceSemaphore
class UserQueryRepositoryServiceImpl implements UserQueryRepositoryService {

     private final UserQueryRepository userQueryRepository;

     UserQueryRepositoryServiceImpl(UserQueryRepository userQueryRepository) {
          this.userQueryRepository = userQueryRepository;
     }

     @Override
     public UserResponse findByUsername(String username) {
          return userQueryRepository.findByUsername(username);
     }

     @Override
     public UserResponse findByNickname(String nickname) {
          return userQueryRepository.findByNickname(nickname);
     }

     @Override
     public String findMerchantUidById(UserId userId) {
          return userQueryRepository.findMerchantUidById(userId);
     }

     @Override
     public String findCustomerUidById(UserId userId) {
          return userQueryRepository.findCustomerUidById(userId);
     }

     @Override
     public String findUsernameByPhone(String phone) {
          return userQueryRepository.findUsernameByPhone(phone);
     }
}
