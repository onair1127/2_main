package com.anseong.core.infrastructure.repository.hibernate.report_image;

import static com.anseong.core.domain.model.report.QReportImage.reportImage;
import static com.anseong.core.domain.model.store.QImageStore.imageStore;
import static com.anseong.core.domain.model.user.QImageUser.imageUser;
import static com.anseong.core.domain.model.image.QImage.image;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.anseong.core.application.queries.report.ReportImageResponse;
import com.anseong.core.domain.model.image.response.ImageDetailResponse;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.report.ReportImageId;
import com.anseong.core.domain.model.report.repository.ReportImageQueryRepository;
import com.anseong.core.domain.model.store.response.ImageStoreResponse;
import com.anseong.core.domain.model.user.response.ImageUserResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
class HibernateReportImageQueryRepository implements ReportImageQueryRepository {

     private final JPAQueryFactory jpaQueryFactory;

     HibernateReportImageQueryRepository(JPAQueryFactory jpaQueryFactory) {
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public List<ReportImageResponse> findWhereNotSolved(ReportImageId lastReportImageId, int amount) {
          List<Long> ids = jpaQueryFactory
               .select(reportImage.id)
               .from(reportImage)
               .where(
                    lastReportImageId == null ? null :reportImage.id.lt(lastReportImageId.value()),
                    reportImage.solved.isFalse()
               )
               .orderBy(reportImage.id.desc())
               .limit(amount)
               .fetch();

          return findByRawIds(ids);
     }

     private List<ReportImageResponse> findByRawIds(List<Long> ids) {
          return jpaQueryFactory
               .select(Projections.constructor(ReportImageResponse.class,
                    reportImage.id
                    , reportImage.image.id
                    , reportImage.userId
                    , reportImage.imageUrl
                    , reportImage.content
                    , reportImage.solved
                    , reportImage.reporters
                    , reportImage.createdDate,
                    Projections.constructor(ImageResponse.class,
                    image.id
                    , image.user.id
                    , image.store.id
                    , image.title
                    , image.price
                    , image.description
                    , image.type
                    , image.job
                    , Projections.constructor(ImageDetailResponse.class,
                         image.detail.filename
                         , image.detail.thumbnail
                         , image.detail.card
                         , image.detail.filter
                         , image.detail.isSquare
                         , image.detail.archived
                         , image.detail.createdDate
                    )
                    , Projections.constructor(ImageUserResponse.class,
                         imageUser.nickname
                         , imageUser.profile
                    )
                    , Projections.constructor(ImageStoreResponse.class,
                         imageStore.id
                         , imageStore.storeName
                         , imageStore.address
                         , imageStore.description
                         , imageStore.thumbnail
                         , imageStore.adult
                         , imageStore.poster
                         , imageStore.filter
                         , imageStore.openingHours
                    )
               )
               ))
               .from(reportImage)
               .join(reportImage.image, image)
               .join(image.user, imageUser)
               .join(image.store, imageStore)
               .where(reportImage.id.in(ids))
               .fetch();
     }
}
