package com.anseong.core.domain.model.user.repository;

import com.anseong.core.domain.model.user.ImageUser;
import com.anseong.core.domain.model.user.User;
import com.anseong.core.domain.model.user.UserId;

public interface UserCommandRepository {

	void save(User newUser);

     void updatePaymentUid(UserId userId, String customerUid, String merchantUid);

     void updateArchived(UserId userId, boolean archived);

     Long resetPassword(String username, String temporaryPassword, String phone);

     Long updatePassword(String username, String newPassword, String previousPassword);

     void updateToAdultByUserId(UserId userId);

     void updateCeoInfo(UserId userId, String nickname);

     void updateNicknameById(UserId userId, String nickname);

     void updateBirthAndPhoneById(UserId userId, int birth, String phone);

     void updateCeoAccount(UserId userId, String username, String password, String phone, int birth);

     void updateProfileById(UserId userId, String nickname, String profile, String logo);

     ImageUser getImageUserReferenceById(UserId id);
}
