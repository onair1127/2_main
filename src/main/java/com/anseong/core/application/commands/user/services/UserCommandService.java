package com.anseong.core.application.commands.user.services;

import com.anseong.core.application.commands.user.AddUserFcmTokenCommand;
import com.anseong.core.application.commands.user.CreateCeoAccountCommand;
import com.anseong.core.application.commands.user.RegistrationCommand;
import com.anseong.core.application.commands.user.ResetMyPasswordCommand;
import com.anseong.core.application.commands.user.UpdateMyPasswordCommand;
import com.anseong.core.application.commands.user.UpdateMyProfileCommand;
import com.anseong.core.application.commands.user.UserType;
import com.anseong.core.domain.client.certification.exception.CertificationFailedException;
import com.anseong.core.domain.client.notificationn.NotificationTopic;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.exception.FirebaseMessagingProcessFailedException;
import com.anseong.core.domain.model.user.exception.NicknameExistsException;
import com.anseong.core.domain.model.user.exception.RegistrationException;

public interface UserCommandService {

	void register(RegistrationCommand command, UserType userType) throws RegistrationException, CertificationFailedException;

	SimpleUser registerCEO(RegistrationCommand command) throws RegistrationException, CertificationFailedException;

     void signout(UserId userId, boolean isCeo) throws FirebaseMessagingProcessFailedException;

     void resetMyPassword(ResetMyPasswordCommand command) throws CertificationFailedException;

     void updateMyPassword(UpdateMyPasswordCommand command) throws CertificationFailedException;

     void setToAdult(UserId userId);

     void updateNickname(UserId userId, String nickname);

     SimpleUser createCeoAccount(CreateCeoAccountCommand command) throws RegistrationException;

     void registerNewCeo(UserId userId, RegistrationCommand command) throws CertificationFailedException;

     void updateMyProfile(UserId userId, UpdateMyProfileCommand command) throws NicknameExistsException;

     boolean addFCMToken(UserId userId, AddUserFcmTokenCommand command, boolean isCeo, boolean isAdult);

     void subscribeTopic(UserId userId, AddUserFcmTokenCommand command, NotificationTopic topic, boolean isCeo, boolean isAdult);

     void unsubscribeTopic(UserId userId, String token, String OS, NotificationTopic topic, boolean isCeo, boolean isAdult);
}
