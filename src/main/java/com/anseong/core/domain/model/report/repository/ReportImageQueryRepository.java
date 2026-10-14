package com.anseong.core.domain.model.report.repository;

import java.util.List;

import com.anseong.core.application.queries.report.ReportImageResponse;
import com.anseong.core.domain.model.report.ReportImageId;

public interface ReportImageQueryRepository {
     List<ReportImageResponse> findWhereNotSolved(ReportImageId lastReportImageId, int amount);
}
