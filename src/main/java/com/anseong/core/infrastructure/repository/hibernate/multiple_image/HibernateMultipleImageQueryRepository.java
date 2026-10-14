package com.anseong.core.infrastructure.repository.hibernate.multiple_image;

import static com.anseong.core.domain.model.multiple_image.QMultipleImage.multipleImage;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.board.Board;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.response.ImageDetailResponse;
import com.anseong.core.domain.model.multiple_image.repository.MultipleImageQueryRepository;
import com.anseong.core.domain.model.multiple_image.response.MultipleImageResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
class HibernateMultipleImageQueryRepository implements MultipleImageQueryRepository {

     private final JPAQueryFactory jpaQueryFactory;

     HibernateMultipleImageQueryRepository(JPAQueryFactory jpaQueryFactory) {
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public List<MultipleImageResponse> findByImageIds(Collection<ImageId> imageIds) {
          return selectFromMultipleImage()
               .from(multipleImage)
               .where(multipleImage.image.id.in(imageIds.stream().map(e -> e.value()).toList()))
               .fetch();
     }

     @Override
     public List<MultipleImageResponse> findByImageId(ImageId imageId) {
          return selectFromMultipleImage()
               .where(multipleImage.image.id.eq(imageId.value()))
               .fetch();
     }

     private JPAQuery<MultipleImageResponse> selectFromMultipleImage() {
          return jpaQueryFactory
               .select(Projections.constructor(MultipleImageResponse.class,
                    multipleImage.id
                    , multipleImage.image.id
                    , multipleImage.userId
                    , multipleImage.sequence
                    , Projections.constructor(ImageDetailResponse.class,
                         multipleImage.detail.filename
                         , multipleImage.detail.thumbnail
                         , multipleImage.detail.card
                         , multipleImage.detail.filter
                         , multipleImage.detail.isSquare
                         , multipleImage.detail.archived
                         , multipleImage.detail.createdDate
                    )
               ))
               .from(multipleImage);
     }
}
