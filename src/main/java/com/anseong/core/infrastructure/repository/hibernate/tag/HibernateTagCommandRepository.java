package com.anseong.core.infrastructure.repository.hibernate.tag;

import static com.anseong.core.domain.model.tag.QTag.tag;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.tag.Tag;
import com.anseong.core.domain.model.tag.repository.TagCommandRepository;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.infrastructure.repository.hibernate.HibernateSupport;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
class HibernateTagCommandRepository extends HibernateSupport<Tag> implements TagCommandRepository {

     private final JPAQueryFactory jpaQueryFactory;

     public HibernateTagCommandRepository(EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
          super(entityManager);
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public List<Tag> findByImageId(ImageId imageId) {
          return jpaQueryFactory
               .selectFrom(tag)
               .where(tag.image.id.eq(imageId.value()))
               .fetch();
     }

     @Override
     public List<Tag> findByUserId(UserId userId) {
          return jpaQueryFactory
               .selectFrom(tag)
               .where(tag.userId.eq(userId.value()))
               .fetch();
     }

     @Override
     public void deleteByUserIdAndTagName(UserId userId, String tagName) {
          jpaQueryFactory
               .delete(tag)
               .where(
                    tag.userId.eq(userId.value()),
                    tag.tagName.eq(tagName)
               )
               .execute();
     }

     @Override
     public void deleteByImageIdAndTagName(ImageId imageId, String tagName) {
          jpaQueryFactory
               .delete(tag)
               .where(
                    tag.image.id.eq(imageId.value()),
                    tag.tagName.eq(tagName)
               )
               .execute();
     }
     @Override
     public Tag findPersonalTagByImageIdAndUserId(ImageId imageId, UserId userId, String tagName) {
          return jpaQueryFactory
               .selectFrom(tag)
               .where(
                    tag.image.id.eq(imageId.value()),
                    tag.userId.eq(userId.value()),
                    tag.tagName.eq(tagName)
               )
               .fetchFirst();
     }
}
