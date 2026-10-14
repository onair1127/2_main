package com.anseong.core.application.commands.report.services.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.commands.report.ReportImageCommand;
import com.anseong.core.application.commands.report.ReportServiceCommand;
import com.anseong.core.application.commands.report.services.ReportCommandRepositoryService;
import com.anseong.core.domain.aop.semaphore.MasterDatasourceSemaphore;
import com.anseong.core.domain.model.image.Image;
import com.anseong.core.domain.model.image.repository.ImageCommandRepository;
import com.anseong.core.domain.model.report.ReportImage;
import com.anseong.core.domain.model.report.ReportImageId;
import com.anseong.core.domain.model.report.repository.ReportImageCommandRepository;
import com.anseong.core.domain.model.report_service.ReportService;
import com.anseong.core.domain.model.report_service.repository.ReportServiceCommandRepository;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.global.utils.JsonUtils;

@Service
@Transactional
@MasterDatasourceSemaphore
class ReportCommandRepositoryServiceImpl implements ReportCommandRepositoryService {

     private final ReportImageCommandRepository reportImageCommandRepository;
     private final ImageCommandRepository imageCommandRepository;
     private final ReportServiceCommandRepository reportServiceCommandRepository;

     ReportCommandRepositoryServiceImpl(
          ReportImageCommandRepository reportImageCommandRepository
          , ImageCommandRepository imageCommandRepository
          , ReportServiceCommandRepository reportServiceCommandRepository
     ) {
          this.reportImageCommandRepository = reportImageCommandRepository;
          this.imageCommandRepository = imageCommandRepository;
          this.reportServiceCommandRepository = reportServiceCommandRepository;
     }

     @Override
     public void saveImage(UserId userId, ReportImageCommand command) {
          ReportImage savedReportImage = reportImageCommandRepository.findByImageId(command.imageId());

          if (savedReportImage != null) {
               record ReporteImageContent(Long id, String content) {}
               String reporters = savedReportImage.getReporters();

               if (reporters == null || reporters.isBlank()) {
                    ReporteImageContent newReporterContent = new ReporteImageContent(userId.value(), command.content());
                    reporters = JsonUtils.toJson(newReporterContent);

                    savedReportImage.updateReporters(reporters);
               } else {
                    ReporteImageContent[] reporterContents = JsonUtils.toObject(reporters, ReporteImageContent[].class);
                    List<ReporteImageContent> reporterList = Arrays.asList(reporterContents);
                    boolean alreadyReported = reporterList.stream()
                         .map(e -> e.id())
                         .anyMatch(e -> e == userId.value());

                    if (alreadyReported) return;

                    reporterList.add(new ReporteImageContent(userId.value(), command.content()));
                    savedReportImage.updateReporters(JsonUtils.toJson(reporterList));
               }
          } else {
               Image imageReference = imageCommandRepository.getReferenceById(command.imageId());
               ReportImage reportImage = ReportImage.create(userId, command, imageReference);

               reportImageCommandRepository.save(reportImage);
          }
     }

     @Override
     public void saveService(UserId userId, ReportServiceCommand command) {
          ReportService reportService = ReportService.create(userId, command);

          reportServiceCommandRepository.save(reportService);
     }

     @Override
     public void solveImage(ReportImageId reportImageId) {
          reportImageCommandRepository.solve(reportImageId);
     }
}
