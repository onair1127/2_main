package com.anseong.core.application.commands.user.services;

import com.anseong.core.application.commands.user.CreateCeoAccountCommand;
import com.anseong.core.application.commands.user.RegistrationCommand;
import com.anseong.core.application.commands.user.UserType;
import com.anseong.core.domain.client.certification.response.CertificationResponse;
import com.anseong.core.domain.model.user.User;
import com.anseong.core.domain.model.user.UserId;

public interface UserCommandRepositoryService {
     User save(RegistrationCommand command, CertificationResponse certificationResponse, UserType userType);

     User saveCEOFromAdmin(CreateCeoAccountCommand command);

     Long resetPassword(String username, String temporaryPassword, String phone);

     Long updatePassword(String username, String temporaryPassword, String previousPassword);

     void updateToAdultByUserId(UserId userId);

     void updateNicknameById(UserId userId, String nickname);

     void updateCeoAccount(UserId userId, RegistrationCommand command, CertificationResponse certificationResponse);

     void updateProfileById(UserId userId, String nickname, String profile, String logo);

     void updateArchived(UserId userId, boolean archived);
}
