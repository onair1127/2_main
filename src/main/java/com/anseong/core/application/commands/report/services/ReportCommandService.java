package com.anseong.core.application.commands.report.services;

import com.anseong.core.application.commands.report.ReportImageCommand;
import com.anseong.core.application.commands.report.ReportServiceCommand;
import com.anseong.core.domain.model.report.ReportImageId;
import com.anseong.core.domain.model.user.UserId;

public interface ReportCommandService {
	void reportImage(UserId userId, ReportImageCommand command);

     void reportService(UserId userId, ReportServiceCommand command);

     void completeReportImage(ReportImageId reportImageId);
}
