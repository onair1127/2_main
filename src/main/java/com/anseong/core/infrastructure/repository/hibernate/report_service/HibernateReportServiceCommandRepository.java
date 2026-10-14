package com.anseong.core.infrastructure.repository.hibernate.report_service;

import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.report_service.ReportService;
import com.anseong.core.domain.model.report_service.repository.ReportServiceCommandRepository;
import com.anseong.core.infrastructure.repository.hibernate.HibernateSupport;

import jakarta.persistence.EntityManager;

@Repository
class HibernateReportServiceCommandRepository extends HibernateSupport<ReportService> implements ReportServiceCommandRepository {

     HibernateReportServiceCommandRepository(EntityManager entityManager) {
          super(entityManager);
     }
}
