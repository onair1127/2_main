package com.anseong.core.application.queries.report.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.queries.report.ReportImageResponse;
import com.anseong.core.application.queries.report.services.ReportImageQueryRepositoryService;
import com.anseong.core.domain.aop.semaphore.ReplicaDatasourceSemaphore;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.multiple_image.repository.MultipleImageQueryRepository;
import com.anseong.core.domain.model.multiple_image.response.MultipleImageResponse;
import com.anseong.core.domain.model.report.ReportImageId;
import com.anseong.core.domain.model.report.repository.ReportImageQueryRepository;
import com.anseong.core.domain.model.tag.repository.TagQueryRepository;
import com.anseong.core.domain.model.tag.response.TagResponse;

@Service
@Transactional(readOnly = true)
@ReplicaDatasourceSemaphore
class ReportQueryRepositoryServiceImpl implements ReportImageQueryRepositoryService {

     private final ReportImageQueryRepository reportImageQueryRepository;
     private final MultipleImageQueryRepository multipleImageQueryRepository;
     private final TagQueryRepository tagQueryRepository;

     ReportQueryRepositoryServiceImpl(
          ReportImageQueryRepository reportImageQueryRepository
          , MultipleImageQueryRepository multipleImageQueryRepository
          , TagQueryRepository tagQueryRepository
     ) {
          this.reportImageQueryRepository = reportImageQueryRepository;
          this.multipleImageQueryRepository = multipleImageQueryRepository;
          this.tagQueryRepository = tagQueryRepository;
     }

     @Override
     public List<ReportImageResponse> findIfNotSolved(ReportImageId lastReportImageId, int amount) {
          List<ReportImageResponse> reportImageResponses = reportImageQueryRepository.findWhereNotSolved(lastReportImageId, amount);

          Set<ImageId> imageIds = reportImageResponses.stream()
               .map(e -> e.imageId())
               .collect(Collectors.toSet());

          Map<Long, List<MultipleImageResponse>> multipleImageResponses = multipleImageQueryRepository.findByImageIds(imageIds).stream()
               .collect(Collectors.groupingBy(e -> e.imageId().value()));
          Map<Long, List<TagResponse>> tagResponses = tagQueryRepository.findByImageIds(imageIds).stream()
               .collect(Collectors.groupingBy(e -> e.imageId().value()));

          reportImageResponses = reportImageResponses.stream()
               .map(e -> {
                    List<TagResponse> tags = tagResponses.get(e.id().value());
                    List<MultipleImageResponse> multipleImages = multipleImageResponses.get(e.id().value());

                    return e.withImageCollections(null, tags, multipleImages);
               })
               .toList();

          return reportImageResponses;
     }
}
