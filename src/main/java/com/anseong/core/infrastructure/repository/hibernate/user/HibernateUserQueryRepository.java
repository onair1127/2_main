package com.anseong.core.infrastructure.repository.hibernate.user;

import static com.anseong.core.domain.model.user.QUser.user;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.repository.UserQueryRepository;
import com.anseong.core.domain.model.user.response.UserResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
class HibernateUserQueryRepository implements UserQueryRepository {

     private final JPAQueryFactory jpaQueryFactory;

     public HibernateUserQueryRepository(JPAQueryFactory jpaQueryFactory) {
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public UserResponse findByUsername(String username) {
          return selectUserResponseFromUser()
               .where(user.username.eq(username))
               .fetchFirst();
     }

     @Override
     public UserResponse findByNickname(String nickname) {
          return selectUserResponseFromUser()
               .where(user.nickname.eq(nickname))
               .fetchFirst();
     }

     @Override
     public UserResponse findByUserId(UserId userId) {
          return selectUserResponseFromUser()
               .where(user.id.eq(userId.value()))
               .fetchFirst();
     }

     @Override
     public String findMerchantUidById(UserId userId) {
          return jpaQueryFactory
               .select(user.merchantUid)
               .from(user)
               .where(user.id.eq(userId.value()))
               .fetchFirst();
     }

     @Override
     public String findCustomerUidById(UserId userId) {
          return jpaQueryFactory
               .select(user.customerUid)
               .from(user)
               .where(user.id.eq(userId.value()))
               .fetchFirst();
     }

     @Override
     public boolean existsByPhone(String phone) {
          Long id = jpaQueryFactory
               .select(user.id)
               .from(user)
               .where(user.phone.eq(phone))
               .fetchFirst();

          // return ids.isEmpty() ? false : true;
          return id != null;
     }

     @Override
     public boolean existsByNickname(String nickname) {
          Long id = jpaQueryFactory
               .select(user.id)
               .from(user)
               .where(user.nickname.eq(nickname))
               .fetchFirst();

          return id != null;
     }

     @Override
     public UserId findIdByPhone(String phone) {
          return new UserId(
               jpaQueryFactory
                    .select(user.id)
                    .from(user)
                    .where(user.phone.eq(phone))
                    .fetchFirst()
          );
     }

     @Override
     public String findUsernameByPhone(String phone) {
          return jpaQueryFactory
               .select(user.username)
               .from(user)
               .where(user.phone.eq(phone))
               .fetchFirst();
     }

     private JPAQuery<UserResponse> selectUserResponseFromUser() {
          return jpaQueryFactory
               .select(Projections.constructor(UserResponse.class,
                    user.id
                    , user.username
                    , user.nickname
                    , user.ceoId
                    , user.password
                    , user.createdDate
                    , user.customerUid
                    , user.merchantUid
                    , user.phone
                    , user.adult
                    , user.archived
                    , user.storeCreated
                    , user.birth
                    , user.profile
                    , user.logo
               ))
               .from(user);
     }
}
