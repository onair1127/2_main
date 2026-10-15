package com.anseong.core.application.queries.report.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anseong.core.application.queries.report.ReportImageResponse;
import com.anseong.core.application.queries.report.services.ReportImageQueryRepositoryService;
import com.anseong.core.application.queries.report.services.ReportImageQueryService;
import com.anseong.core.domain.model.report.ReportImageId;

@Service
class ReportQueryServiceImpl implements ReportImageQueryService {

     private final ReportImageQueryRepositoryService reportQueryRepositoryService;

     ReportQueryServiceImpl(ReportImageQueryRepositoryService reportQueryRepositoryService) {
          this.reportQueryRepositoryService = reportQueryRepositoryService;
     }

     @Override
     public List<ReportImageResponse> getReportImages(ReportImageId lastReportImageId) {
          return reportQueryRepositoryService.findIfNotSolved(lastReportImageId, 12);
     }
}
