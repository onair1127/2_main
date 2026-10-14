package com.anseong.core.infrastructure.repository.hibernate.user;

import static com.anseong.core.domain.model.user.QUser.user;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.user.ImageUser;
import com.anseong.core.domain.model.user.User;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.repository.UserCommandRepository;
import com.anseong.core.infrastructure.repository.hibernate.HibernateSupport;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
class HibernateUserCommandRepository extends HibernateSupport<User> implements UserCommandRepository {

     private final JPAQueryFactory jpaQueryFactory;

     public HibernateUserCommandRepository(EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
          super(entityManager);
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public void updatePaymentUid(UserId userId, String customerUid, String merchantUid) {
          jpaQueryFactory
               .update(user)
               .set(user.customerUid, customerUid)
               .set(user.merchantUid, merchantUid)
               .where(user.id.eq(userId.value()))
               .execute();
     }

     @Override
     public void updateArchived(UserId userId, boolean archived) {
          jpaQueryFactory
               .update(user)
               .set(user.archived, archived)
               .where(user.id.eq(userId.value()))
               .execute();
     }

     @Override
     public Long resetPassword(String username, String temporaryPassword, String phone) {
          return jpaQueryFactory
               .update(user)
               .set(user.password, temporaryPassword)
               .where(
                    user.username.eq(username),
                    user.phone.eq(phone)
               )
               .execute();
     }

     @Override
     public Long updatePassword(String username, String newPassword, String previousPassword) {
          return jpaQueryFactory
               .update(user)
               .set(user.password, newPassword)
               .where(
                    user.username.eq(username),
                    user.password.eq(previousPassword)
               )
               .execute();
     }

     @Override
     public void updateToAdultByUserId(UserId userId) {
          jpaQueryFactory
               .update(user)
               .set(user.adult, true)
               .where(user.id.eq(userId.value()))
               .execute();
     }

     @Override
     public void updateCeoInfo(UserId userId, String nickname) {
          jpaQueryFactory
               .update(user)
               .set(user.nickname, nickname)
               .set(user.storeCreated, true)
               .where(user.id.eq(userId.value()));
     }

     @Override
     public void updateNicknameById(UserId userId, String nickname) {
          jpaQueryFactory
               .update(user)
               .set(user.nickname, nickname)
               .where(user.id.eq(userId.value()))
               .execute();
     }

     @Override
     public void updateBirthAndPhoneById(UserId userId, int birth, String phone) {
          jpaQueryFactory
               .update(user)
               .set(user.birth, birth)
               .set(user.phone, phone)
               .where(user.id.eq(userId.value()))
               .execute();
     }

     @Override
     public void updateCeoAccount(UserId userId, String username, String password, String phone, int birth) {
          jpaQueryFactory
               .update(user)
               .set(user.username, username)
               .set(user.password, password)
               .set(user.phone, phone)
               .set(user.birth, birth)
               .where(user.id.eq(userId.value()))
               .execute();
     }

     @Override
     public void updateProfileById(UserId userId, String nickname, String profile, String logo) {
          jpaQueryFactory
               .update(user)
               .set(user.nickname, nickname)
               .set(user.profile, profile)
               .set(user.logo, logo)
               .where(user.id.eq(userId.value()))
               .execute();
     }

     @Override
     public ImageUser getImageUserReferenceById(UserId id) {
          return getReference(ImageUser.class, id.value());
     }
}
