package com.anseong.core.infrastructure.repository.hibernate.image;

import static com.anseong.core.domain.model.image.QImage.image;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.image.Image;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.repository.ImageCommandRepository;
import com.anseong.core.infrastructure.repository.hibernate.HibernateSupport;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
class HibernateImageCommandRepository extends HibernateSupport<Image> implements ImageCommandRepository {

     private final JPAQueryFactory jpaQueryFactory;

     HibernateImageCommandRepository(EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
          super(entityManager);
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public Image findById(ImageId imageId) {
          return find(imageId.value());
     }

     @Override
     public Image getReferenceById(ImageId imageId) {
          return getReference(imageId.value());
     }

     @Override
     public void removeById(ImageId imageId) {
          jpaQueryFactory
               .update(image)
               .set(image.detail.archived, true)
               .where(image.id.eq(imageId.value()))
               .execute();
     }

     @Override
     public ImageId saveAndReturn(Image image) {
          save(image);
          return image.getId();
     }
}
