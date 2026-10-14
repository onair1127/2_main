package com.anseong.core.infrastructure.repository.hibernate.tag;

import static com.anseong.core.domain.model.tag.QStoreTag.storeTag;
import static com.anseong.core.domain.model.tag.QTag.tag;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.tag.repository.TagQueryRepository;
import com.anseong.core.domain.model.tag.response.StoreTagResponse;
import com.anseong.core.domain.model.tag.response.TagResponse;
import com.anseong.core.domain.model.user.UserId;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
class HibernateTagQueryRepository implements TagQueryRepository {

     private final JPAQueryFactory jpaQueryFactory;

     public HibernateTagQueryRepository(JPAQueryFactory jpaQueryFactory) {
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public List<TagResponse> findByImageId(ImageId id) {
          return selectFromTag()
               .where(tag.image.id.eq(id.value()))
               .fetch();
     }

     @Override
     public List<TagResponse> findByImageIds(Collection<ImageId> imageIds) {
          return selectFromTag()
               .where(tag.image.id.in(imageIds.stream().map(e -> e.value()).toList()))
               .fetch();
     }

     @Override
     public List<StoreTagResponse> findStoreTagsByUserId(UserId userId) {
          return selectFromStoreTag()
               .where(storeTag.userId.eq(userId.value()))
               .fetch();
     }

     @Override
     public List<StoreTagResponse> findStoreTagsByUserIds(Collection<UserId> userIds) {
          return selectFromStoreTag()
               .where(storeTag.userId.in(userIds.stream().map(e -> e.value()).toList()))
               .fetch();
     }

     @Override
     public List<String> findCommonTagNamesByUserId(UserId userId) {
          return jpaQueryFactory
               .selectDistinct(tag.tagName)
               .from(tag)
               .where(
                    tag.userId.eq(userId.value()),
                    tag.type.eq("common")
               )
               .fetch();
     }

     @Override
     public List<String> findTagNamesByKeyword(String keyword, int amount) {
          return jpaQueryFactory
               .selectDistinct(tag.tagName)
               .from(tag)
               .where(tag.tagName.contains(keyword))
               .limit(amount)
               .fetch();
     }

     private JPAQuery<TagResponse> selectFromTag() {
          return jpaQueryFactory
               .select(Projections.constructor(TagResponse.class,
                    tag.image.id,
                    tag.userId,
                    tag.tagName,
                    tag.type
               ))
               .from(tag);
     }

     private JPAQuery<StoreTagResponse> selectFromStoreTag() {
          return jpaQueryFactory
               .select(Projections.constructor(StoreTagResponse.class,
                    storeTag.userId
                    , storeTag.tagName
               ))
               .from(storeTag);
     }
}
