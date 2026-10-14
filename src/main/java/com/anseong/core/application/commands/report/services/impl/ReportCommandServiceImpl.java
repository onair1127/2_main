package com.anseong.core.application.commands.report.services.impl;

import org.springframework.stereotype.Service;

import com.anseong.core.application.commands.report.ReportImageCommand;
import com.anseong.core.application.commands.report.ReportServiceCommand;
import com.anseong.core.application.commands.report.services.ReportCommandRepositoryService;
import com.anseong.core.application.commands.report.services.ReportCommandService;
import com.anseong.core.domain.client.messanger.MessangerClient;
import com.anseong.core.domain.client.messanger.MessangerTemplate;
import com.anseong.core.domain.client.messanger.MessangerType;
import com.anseong.core.domain.client.messanger.request.MessangerRequest;
import com.anseong.core.domain.model.report.ReportImageId;
import com.anseong.core.domain.model.user.UserId;

@Service
class ReportCommandServiceImpl implements ReportCommandService {

     private final ReportCommandRepositoryService reportCommandRepositoryService;
     private final MessangerClient messangerClient;

     public ReportCommandServiceImpl(
          ReportCommandRepositoryService reportCommandRepositoryService,
          MessangerClient messangerClient
     ) {
          this.reportCommandRepositoryService = reportCommandRepositoryService;
          this.messangerClient = messangerClient;
     }

     @Override
     public void reportImage(UserId userId, ReportImageCommand command) {
          reportCommandRepositoryService.saveImage(userId, command);
     }

     @Override
     public void reportService(UserId userId, ReportServiceCommand command) {
          reportCommandRepositoryService.saveService(userId, command);
          MessangerRequest request = MessangerRequest.builder()
               .type(MessangerType.SMS)
               .template(MessangerTemplate.ADMIN_REPORT_SERVICE)
               .username(command.phone())
               .build();

          messangerClient.sendReportSMSNotificationToMe(request);
     }

     @Override
     public void completeReportImage(ReportImageId reportImageId) {
          reportCommandRepositoryService.solveImage(reportImageId);
     }
}
