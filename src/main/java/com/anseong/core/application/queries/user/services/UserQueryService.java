package com.anseong.core.application.queries.user.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.anseong.core.application.queries.user.ValidateUserQuery;
import com.anseong.core.domain.client.certification.exception.CertificationFailedException;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.exception.RegistrationException;

public interface UserQueryService extends UserDetailsService {
     String getMerchantUidById(UserId userId);

     String getUsernameByPhone(String uid) throws CertificationFailedException;

     void validateUser(ValidateUserQuery command, boolean nicknameCreated) throws RegistrationException;

     boolean getSubscribeBoardStatus(UserId userId, String oS, String token);
}
