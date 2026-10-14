package com.anseong.core.application.queries.report.services;

import java.util.List;

import com.anseong.core.application.queries.report.ReportImageResponse;
import com.anseong.core.domain.model.report.ReportImageId;

public interface ReportImageQueryService {
     List<ReportImageResponse> getReportImages(ReportImageId lastReportImageId);
}
