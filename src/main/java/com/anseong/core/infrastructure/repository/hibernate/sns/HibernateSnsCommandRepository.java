package com.anseong.core.infrastructure.repository.hibernate.sns;

import static com.anseong.core.domain.model.sns.QSns.sns;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.sns.Sns;
import com.anseong.core.domain.model.sns.repository.SnsCommandRepository;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.infrastructure.repository.hibernate.HibernateSupport;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
class HibernateSnsCommandRepository extends HibernateSupport<Sns> implements SnsCommandRepository {

     private final JPAQueryFactory jpaQueryFactory;

     HibernateSnsCommandRepository(EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
          super(entityManager);
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public void removeByUserId(UserId userId) {
          jpaQueryFactory
               .delete(sns)
               .where(sns.userId.eq(userId.value()))
               .execute();
     }

     @Override
     public void removeByUserIdAndType(UserId userId, String type) {
          jpaQueryFactory
               .delete(sns)
               .where(
                    sns.userId.eq(userId.value()),
                    sns.type.eq(type)
               )
               .execute();
     }
}
