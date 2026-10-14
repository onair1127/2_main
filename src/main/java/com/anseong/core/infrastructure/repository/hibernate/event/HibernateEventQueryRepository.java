package com.anseong.core.infrastructure.repository.hibernate.event;

import static com.anseong.core.domain.model.event.QEvent.event;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.event.repository.EventQueryRepository;
import com.anseong.core.domain.model.user.UserId;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
class HibernateEventQueryRepository implements EventQueryRepository {

     private final JPAQueryFactory jpaQueryFactory;

     HibernateEventQueryRepository(JPAQueryFactory jpaQueryFactory) {
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public boolean existsByUserId(UserId userId) {
          Long id = jpaQueryFactory
               .select(event.id)
               .from(event)
               .where(event.userId.eq(userId.value()))
               .fetchFirst();

          return id != null;
     }
}
