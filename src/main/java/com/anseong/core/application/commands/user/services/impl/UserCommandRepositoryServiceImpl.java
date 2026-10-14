package com.anseong.core.application.commands.user.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.commands.user.CreateCeoAccountCommand;
import com.anseong.core.application.commands.user.RegistrationCommand;
import com.anseong.core.application.commands.user.UserType;
import com.anseong.core.application.commands.user.services.UserCommandRepositoryService;
import com.anseong.core.domain.aop.semaphore.MasterDatasourceSemaphore;
import com.anseong.core.domain.client.certification.response.CertificationResponse;
import com.anseong.core.domain.model.user.User;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.exception.NicknameExistsException;
import com.anseong.core.domain.model.user.exception.PhoneExistsException;
import com.anseong.core.domain.model.user.repository.UserCommandRepository;
import com.anseong.core.domain.model.user.repository.UserQueryRepository;
import com.anseong.core.domain.security.PasswordEncryptor;

@Service
@Transactional
@MasterDatasourceSemaphore
class UserCommandRepositoryServiceImpl implements UserCommandRepositoryService {

     private final UserCommandRepository userCommandRepository;
     private final UserQueryRepository userQueryRepository;
     private final PasswordEncryptor passwordEncryptor;

     UserCommandRepositoryServiceImpl(
          UserCommandRepository userCommandRepository
          , UserQueryRepository userQueryRepository
          , PasswordEncryptor passwordEncryptor
     ) {
          this.userCommandRepository = userCommandRepository;
          this.userQueryRepository = userQueryRepository;
          this.passwordEncryptor = passwordEncryptor;
     }

     @Override
     public User save(RegistrationCommand command, CertificationResponse certificationResponse, UserType userType) {
          boolean existedUser = userQueryRepository.existsByPhone(certificationResponse.phone());

          if (existedUser)
               throw new PhoneExistsException("이미 존재하는 계정입니다.");

          final String encryptedPassword = passwordEncryptor.encrypt(command.password());
          final int birth = Integer.parseInt(certificationResponse.birthday().substring(0, 4));

          User newUser = userType == UserType.CEO
               ? User.createCEO(command.username(), command.nickname(), encryptedPassword, certificationResponse.phone(), birth)
               : User.create(command.username(), command.nickname(), encryptedPassword, certificationResponse.phone(), command.isAdult(), birth);

          userCommandRepository.save(newUser);

          return newUser;
     }

     @Override
     public User saveCEOFromAdmin(CreateCeoAccountCommand command) {
          final String encryptedPassword = passwordEncryptor.encrypt(command.password());
          User newUser = User.createCEOByAdmin(command.username(), command.nickname(), encryptedPassword);

          userCommandRepository.save(newUser);
          return newUser;
     }

     @Override
     public Long resetPassword(String username, String temporaryPassword, String phone) {
          return userCommandRepository.resetPassword(username, temporaryPassword, phone);
     }

     @Override
     public Long updatePassword(String username, String temporaryPassword, String previousPassword) {
          return userCommandRepository.updatePassword(username, temporaryPassword, previousPassword);
     }

     @Override
     public void updateToAdultByUserId(UserId userId) {
          userCommandRepository.updateToAdultByUserId(userId);
     }

     @Override
     public void updateNicknameById(UserId userId, String nickname) {
          userCommandRepository.updateNicknameById(userId, nickname);
     }

     @Override
     public void updateCeoAccount(UserId userId, RegistrationCommand command, CertificationResponse certificationResponse) {
          final String encryptedPassword = passwordEncryptor.encrypt(command.password());
          final int birth = Integer.parseInt(certificationResponse.birthday().substring(0, 4));

          userCommandRepository.updateCeoAccount(userId, command.username(), encryptedPassword, certificationResponse.phone(), birth);
     }

     @Override
     public void updateProfileById(UserId userId, String nickname, String profile, String logo) {
          boolean nicknameExists = userQueryRepository.existsByNickname(nickname);

          if (nicknameExists)
               throw new NicknameExistsException("닉네임이 이미 존재합니다");

          userCommandRepository.updateProfileById(userId, nickname, profile, logo);
     }

     @Override
     public void updateArchived(UserId userId, boolean archived) {
          userCommandRepository.updateArchived(userId, archived);
     }
}
