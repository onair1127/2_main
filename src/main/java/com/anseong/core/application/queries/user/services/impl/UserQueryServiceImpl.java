package com.anseong.core.application.queries.user.services.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.anseong.core.application.queries.user.ValidateUserQuery;
import com.anseong.core.application.queries.user.services.UserQueryRepositoryService;
import com.anseong.core.application.queries.user.services.UserQueryService;
import com.anseong.core.domain.client.certification.CertificationClient;
import com.anseong.core.domain.client.certification.CertificationPlatform;
import com.anseong.core.domain.client.certification.exception.CertificationFailedException;
import com.anseong.core.domain.client.certification.request.CertificationRequest;
import com.anseong.core.domain.client.certification.response.CertificationResponse;
import com.anseong.core.domain.client.notificationn.NotificationClient;
import com.anseong.core.domain.client.notificationn.request.BatchImportFromAPNsTokenRequest;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.exception.NicknameExistsException;
import com.anseong.core.domain.model.user.exception.RegistrationException;
import com.anseong.core.domain.model.user.exception.UsernameExistsException;
import com.anseong.core.domain.model.user.repository.UserCache;
import com.anseong.core.domain.model.user.response.UserResponse;

@Service
class UserQueryServiceImpl implements UserQueryService {

     private final UserQueryRepositoryService userQueryRepositoryService;
     private final UserCache userCache;
     private final NotificationClient messageClient;
     private final CertificationClient certificationClient;

     UserQueryServiceImpl(
          UserQueryRepositoryService userQueryRepositoryService,
          UserCache userCache,
          NotificationClient messageClient,
          CertificationClient certificationClient
     ) {
          this.userQueryRepositoryService = userQueryRepositoryService;
          this.userCache = userCache;
          this.messageClient = messageClient;
          this.certificationClient = certificationClient;
     }

     @Override
     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
          Assert.hasText(username, "`username` 값이 비어있습니다.");
          UserResponse user = userQueryRepositoryService.findByUsername(username);

          if (user == null) {
               user = userQueryRepositoryService.findByNickname(username);
          }

          if (user == null) {
               throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
          }

		return SimpleUser.from(user);
	}

     @Override
     public String getMerchantUidById(UserId userId) {
          return userQueryRepositoryService.findMerchantUidById(userId);
     }

     @Override
     public String getUsernameByPhone(String uid) throws CertificationFailedException {
          final CertificationRequest certificationRequest = CertificationRequest.builder()
               .platform(CertificationPlatform.IAMPORT)
               .uid(uid)
               .build();

          final CertificationResponse certificationResponse = certificationClient.getCertificationInfo(certificationRequest);
          final String username = userQueryRepositoryService.findUsernameByPhone(certificationResponse.phone());

          if (username == null) {
               throw new CertificationFailedException("가입된 회원이 아닙니다.");
          } else {
               return username;
          }
     }

     @Override
     public void validateUser(ValidateUserQuery command, boolean nicknameCreated) throws RegistrationException {
          UserResponse user = userQueryRepositoryService.findByUsername(command.username());

          if (user != null) {
               throw new UsernameExistsException("이미 존재하는 아이디");
          }

          if (!nicknameCreated) {
               user = userQueryRepositoryService.findByNickname(command.nickname());
               if (user != null) {
                    throw new NicknameExistsException("이미 존재하는 닉네임");
               }
          }
     }

     @Override
     public boolean getSubscribeBoardStatus(UserId userId, String OS, String token) {
          if (OS.equals("ios")) {
               BatchImportFromAPNsTokenRequest request = BatchImportFromAPNsTokenRequest.builder()
                    .APNs(token)
                    .build();

               token = messageClient.batchImportFromAPNsToken(request);
          }

          return userCache.getSubscribeBoardStatus(token);
     }
}
