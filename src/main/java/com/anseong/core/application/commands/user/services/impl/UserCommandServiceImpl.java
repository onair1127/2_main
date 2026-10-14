package com.anseong.core.application.commands.user.services.impl;

import com.anseong.core.application.commands.user.AddUserFcmTokenCommand;
import com.anseong.core.application.commands.user.CreateCeoAccountCommand;
import com.anseong.core.application.commands.user.RegistrationCommand;
import com.anseong.core.application.commands.user.ResetMyPasswordCommand;
import com.anseong.core.application.commands.user.UpdateMyPasswordCommand;
import com.anseong.core.application.commands.user.UpdateMyProfileCommand;
import com.anseong.core.application.commands.user.UserType;
import com.anseong.core.application.commands.user.services.UserCommandRepositoryService;
import com.anseong.core.application.commands.user.services.UserCommandService;
import com.anseong.core.application.queries.user.services.UserQueryRepositoryService;
import com.anseong.core.domain.client.certification.CertificationClient;
import com.anseong.core.domain.client.certification.exception.CertificationFailedException;
import com.anseong.core.domain.client.certification.request.CertificationRequest;
import com.anseong.core.domain.client.certification.response.CertificationResponse;
import com.anseong.core.domain.client.messanger.MessangerClient;
import com.anseong.core.domain.client.notificationn.NotificationClient;
import com.anseong.core.domain.client.notificationn.NotificationTopic;
import com.anseong.core.domain.client.notificationn.request.BatchImportFromAPNsTokenRequest;
import com.anseong.core.domain.client.notificationn.request.SubscribeRequest;
import com.anseong.core.domain.client.payment.PaymentClient;
import com.anseong.core.domain.client.payment.request.ScheduleRequest;
import com.anseong.core.domain.model.store.repository.StoreCache;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.domain.model.user.User;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.exception.FirebaseMessagingProcessFailedException;
import com.anseong.core.domain.model.user.exception.NicknameExistsException;
import com.anseong.core.domain.model.user.exception.RegistrationException;
import com.anseong.core.domain.model.user.repository.UserCache;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
class UserCommandServiceImpl implements UserCommandService {

     private final UserQueryRepositoryService userQueryRepositoryService;
     private final UserCommandRepositoryService userCommandRepositoryService;
     private final PaymentClient paymentClient;
     private final CertificationClient certificationClient;
     private final NotificationClient notificationClient;
     private final MessangerClient messangerClient;
     private final StoreCache storeCache;
     private final UserCache userCache;

     UserCommandServiceImpl(
          UserQueryRepositoryService userQueryRepositoryService
          , UserCommandRepositoryService userCommandRepositoryService
          , PaymentClient paymentClient
          , CertificationClient certificationClient
          , NotificationClient notificationClient
          , MessangerClient messangerClient
          , StoreCache storeCache
          , UserCache userCache
     ) {
          this.userQueryRepositoryService = userQueryRepositoryService;
          this.userCommandRepositoryService = userCommandRepositoryService;
          this.paymentClient = paymentClient;
          this.certificationClient = certificationClient;
          this.notificationClient = notificationClient;
          this.messangerClient = messangerClient;
          this.storeCache = storeCache;
          this.userCache = userCache;
     }

     @Override
     public void register(RegistrationCommand command, UserType userType) throws RegistrationException, CertificationFailedException {
          CertificationResponse certificationResponse = getUserCertification(command.impUid());
          UserId savedUserId = userCommandRepositoryService.save(command, certificationResponse, userType).getId();

          userCache.addUserDataFromGuestData(command.favoriteList(), command.boardLikeList(), command.searchHistory(), command.imageSort(), savedUserId);
     }

     @Override
	public SimpleUser registerCEO(RegistrationCommand command) throws RegistrationException, CertificationFailedException {
          CertificationResponse certificationResponse = getUserCertification(command.impUid());

          final User savedCEO = userCommandRepositoryService.save(command, certificationResponse, UserType.CEO);
          // domainEventPublisher.publish(new UserRegisteredEvent(newCEO));
          return new SimpleUser(savedCEO);
	}

     @Override
     public void signout(UserId userId, boolean isCeo) throws FirebaseMessagingProcessFailedException {
          Assert.notNull(userId, "파라미터 `id` 값은 항상 존재하여야 합니다.");

          userCommandRepositoryService.updateArchived(userId, true);

          String deletedToken = userCache.deleteUserFcmToken(userId, isCeo);

          if (deletedToken != null) {
               SubscribeRequest request = SubscribeRequest.builder()
                    .token(deletedToken)
                    .topic(NotificationTopic.BOARD)
                    .isCeo(isCeo)
                    .isAdult(isCeo)
                    .build();

               notificationClient.unsubscribeFromTopicAsync(request);
          }

          if (isCeo) {
               String customerUid = userQueryRepositoryService.findCustomerUidById(userId);
               ScheduleRequest request = ScheduleRequest.builder()
                    .customerUid(customerUid)
                    .build();

               paymentClient.unscheduleCustomerAll(request);
               storeCache.deleteStore(userId);
          }

          // TODO. archived = false
     }

     @Override
     public void resetMyPassword(ResetMyPasswordCommand command) throws CertificationFailedException {
          CertificationResponse certificationResponse = getUserCertification(command.impUid());
          Long result = userCommandRepositoryService.resetPassword(command.impUid(), command.username(), certificationResponse.phone());

          if (result != 1) {
               throw new CertificationFailedException("존재하지 않는 계정입니다.");
          }
     }

     @Override
     public void updateMyPassword(UpdateMyPasswordCommand command) throws CertificationFailedException {
          Long result = userCommandRepositoryService.updatePassword(command.password(), command.username(), command.impUid());

          if (result != 1) {
               throw new CertificationFailedException("아무런 업데이트가 되지 않음.");
          }
     }

     @Override
     public void setToAdult(UserId userId) {
          userCommandRepositoryService.updateToAdultByUserId(userId);
     }

     @Override
     public void updateNickname(UserId userId, String nickname) {
          userCommandRepositoryService.updateNicknameById(userId, nickname);
     }

     @Override
     public SimpleUser createCeoAccount(CreateCeoAccountCommand command) throws RegistrationException {
          User savedUser = userCommandRepositoryService.saveCEOFromAdmin(command);
          return new SimpleUser(savedUser);
     }

     @Override
     public void registerNewCeo(UserId userId, RegistrationCommand command) throws CertificationFailedException {
          CertificationResponse certificationResponse = getUserCertification(command.impUid());
          userCommandRepositoryService.updateCeoAccount(userId, command, certificationResponse);

          userCache.addUserDataFromGuestData(command.favoriteList(), command.boardLikeList(), command.searchHistory(), command.imageSort(), userId);
     }

     @Override
     public void updateMyProfile(UserId userId, UpdateMyProfileCommand command) throws NicknameExistsException {
          Assert.notNull(userId, "파라미터 `id` 값은 항상 존재하여야 합니다.");

          userCommandRepositoryService.updateProfileById(userId, command.nickname(), command.profile(), command.logo());
     }

     private CertificationResponse getUserCertification(String uid) {
          CertificationRequest certificationRequest = CertificationRequest.builder()
               .uid(uid)
               .build();

          return certificationClient.getCertificationInfo(certificationRequest);
     }

     @Override
     public boolean addFCMToken(UserId userId, AddUserFcmTokenCommand command, boolean isCeo, boolean isAdult) {
          String token;
          if (command.OS().equals("ios")) {
               BatchImportFromAPNsTokenRequest request = BatchImportFromAPNsTokenRequest.builder()
                    .APNs(command.token())
                    .build();

               token = notificationClient.batchImportFromAPNsToken(request);

          } else {
               token = command.token();
          }

          boolean hasKey = userCache.addUserFcmToken(userId, token, isCeo, isAdult);

          SubscribeRequest request = SubscribeRequest.builder()
               .token(token)
               .topic(NotificationTopic.BOARD)
               .isCeo(isCeo)
               .isAdult(isAdult)
               .build();

          notificationClient.subscribeToTopicAsync(request);

          return hasKey;
     }

     @Override
     public void subscribeTopic(UserId userId, AddUserFcmTokenCommand command, NotificationTopic topic, boolean isCeo, boolean isAdult) {
          String token;

          if (command.OS().equals("ios")) {
               BatchImportFromAPNsTokenRequest request = BatchImportFromAPNsTokenRequest.builder()
                    .APNs(command.token())
                    .build();

               token = notificationClient.batchImportFromAPNsToken(request);

          } else {
               token = command.token();
          }

          SubscribeRequest request = SubscribeRequest.builder()
               .token(token)
               .topic(topic)
               .isCeo(isCeo)
               .isAdult(isAdult)
               .build();

          notificationClient.subscribeToTopicAsync(request);
          userCache.subscribeBoard(token);
     }

     @Override
     public void unsubscribeTopic(UserId userId, String token, String OS, NotificationTopic topic, boolean isCeo,
               boolean isAdult) {
          if (OS.equals("ios")) {
               BatchImportFromAPNsTokenRequest request = BatchImportFromAPNsTokenRequest.builder()
                    .APNs(token)
                    .build();

               token = notificationClient.batchImportFromAPNsToken(request);
          }

          SubscribeRequest request = SubscribeRequest.builder()
               .token(token)
               .topic(topic)
               .isCeo(isCeo)
               .isAdult(isAdult)
               .build();

          notificationClient.unsubscribeFromTopicAsync(request);
          userCache.unsubscribeBoard(token);
     }
}
