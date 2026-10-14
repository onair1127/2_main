package com.anseong.core.infrastructure.repository.hibernate.sns;

import static com.anseong.core.domain.model.sns.QSns.sns;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.sns.repository.SnsQueryRepository;
import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.user.UserId;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
class HibernateSnsQueryService implements SnsQueryRepository {

     private final JPAQueryFactory jpaQueryFactory;

     HibernateSnsQueryService(JPAQueryFactory jpaQueryFactory) {
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public List<SnsResponse> findByUserId(UserId userId) {
          return jpaQueryFactory
               .select(Projections.constructor(SnsResponse.class,
                    sns.userId
                    , sns.type
                    , sns.url
               ))
               .from(sns)
               .where(sns.userId.eq(userId.value()))
               .fetch();
     }

     @Override
     public List<SnsResponse> findByUserIds(Collection<UserId> userIds) {
          return jpaQueryFactory
               .select(Projections.constructor(SnsResponse.class,
                    sns.userId,
                    sns.type,
                    sns.url
               ))
               .from(sns)
               .where(sns.userId.in(userIds.stream().map(e -> e.value()).toList()))
               .fetch();
     }
}
