package com.anseong.core.application.commands.report.services;

import com.anseong.core.application.commands.report.ReportImageCommand;
import com.anseong.core.application.commands.report.ReportServiceCommand;
import com.anseong.core.domain.model.report.ReportImageId;
import com.anseong.core.domain.model.user.UserId;

public interface ReportCommandRepositoryService {
     void saveImage(UserId userId, ReportImageCommand command);

     void saveService(UserId userId, ReportServiceCommand command);

     void solveImage(ReportImageId reportImageId);
}
