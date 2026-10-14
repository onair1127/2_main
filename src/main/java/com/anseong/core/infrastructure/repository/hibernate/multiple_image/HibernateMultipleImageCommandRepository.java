package com.anseong.core.infrastructure.repository.hibernate.multiple_image;

import static com.anseong.core.domain.model.multiple_image.QMultipleImage.multipleImage;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.multiple_image.MultipleImage;
import com.anseong.core.domain.model.multiple_image.MultipleImageId;
import com.anseong.core.domain.model.multiple_image.repository.MultipleImageCommandRepository;
import com.anseong.core.infrastructure.repository.hibernate.HibernateSupport;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
class HibernateMultipleImageCommandRepository extends HibernateSupport<MultipleImage> implements MultipleImageCommandRepository {

     private final JPAQueryFactory jpaQueryFactory;

     HibernateMultipleImageCommandRepository(EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
          super(entityManager);
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public void removeById(List<MultipleImageId> multipleImageIds) {
          jpaQueryFactory
               .update(multipleImage)
               .set(multipleImage.detail.archived, true)
               .where(multipleImage.id.in(multipleImageIds.stream().map(e -> e.value()).toList()))
               .execute();
     }
}
