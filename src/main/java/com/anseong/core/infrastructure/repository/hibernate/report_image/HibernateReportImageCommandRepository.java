package com.anseong.core.infrastructure.repository.hibernate.report_image;

import static com.anseong.core.domain.model.image.QImage.image;
import static com.anseong.core.domain.model.report.QReportImage.reportImage;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.report.ReportImage;
import com.anseong.core.domain.model.report.ReportImageId;
import com.anseong.core.domain.model.report.repository.ReportImageCommandRepository;
import com.anseong.core.infrastructure.repository.hibernate.HibernateSupport;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
class HibernateReportImageCommandRepository extends HibernateSupport<ReportImage> implements ReportImageCommandRepository {

     private final JPAQueryFactory jpaQueryFactory;

     HibernateReportImageCommandRepository(EntityManager entityManager, JPAQueryFactory jpaQueryFactory) {
          super(entityManager);
          this.jpaQueryFactory = jpaQueryFactory;
     }

     @Override
     public ReportImage findByImageId(ImageId imageId) {
          return jpaQueryFactory
               .selectFrom(reportImage)
               .join(reportImage.image, image)
               .where(image.id.eq(imageId.value()))
               .fetchFirst();
     }

     @Override
     public void solve(ReportImageId id) {
          jpaQueryFactory
               .update(reportImage)
               .set(reportImage.solved, true)
               .where(reportImage.id.eq(id.value()))
               .execute();
     }
}
