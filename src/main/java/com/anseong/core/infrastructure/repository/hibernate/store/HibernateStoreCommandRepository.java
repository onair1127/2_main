package com.anseong.core.infrastructure.repository.hibernate.store;

import static com.anseong.core.domain.model.store.QStore.store;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.store.ImageStore;
import com.anseong.core.domain.model.store.Store;
import com.anseong.core.domain.model.store.repository.StoreCommandRepository;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.infrastructure.repository.hibernate.HibernateSupport;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
class HibernateStoreCommandRepository extends HibernateSupport<Store> implements StoreCommandRepository {

     private final JPAQueryFactory jpaQueryFactory;

     public HibernateStoreCommandRepository(EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
          super(entityManager);
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public Store findByUserId(UserId storeId) {
          return find(storeId.value());
     }

     @Override
     public Long findDuplicatedStore(UserId storeId, long bizNo) {
          return jpaQueryFactory
               .select(store.userId.count())
               .from(store)
               .where(
                    store.userId.eq(storeId.value()),
                    store.bizNo.eq(bizNo)
               )
               .fetchOne();
     }

     @Override
     public ImageStore getImageStoreReferenceById(UserId userId) {
          return getReference(ImageStore.class, userId.value());
     }
}
