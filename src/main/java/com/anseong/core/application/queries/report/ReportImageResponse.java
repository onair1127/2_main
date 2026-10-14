package com.anseong.core.application.queries.report;

import java.time.LocalDateTime;
import java.util.List;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.multiple_image.response.MultipleImageResponse;
import com.anseong.core.domain.model.report.ReportImageId;
import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.tag.response.TagResponse;
import com.anseong.core.domain.model.user.UserId;

public record ReportImageResponse(
     ReportImageId id,
     ImageId imageId,
     UserId userId,
     String imageUrl,
     String content,
     boolean solved,
     String reporters,
     LocalDateTime createdDate,
     ImageResponse image
) {
     public ReportImageResponse(
          Long id,
          Long imageId,
          Long userId,
          String imageUrl,
          String content,
          boolean solved,
          String reporters,
          LocalDateTime createdDate
     ) {
          this(
               new ReportImageId(id),
               new ImageId(imageId),
               new UserId(userId),
               imageUrl,
               content,
               solved,
               reporters,
               createdDate
               , null
          );
     }

     public ReportImageResponse withImageCollections(List<SnsResponse> snsResponses, List<TagResponse> tagResponses, List<MultipleImageResponse> multipleImageResponses) {
          return new ReportImageResponse(
               this.id
               , this.imageId
               , this.userId
               , this.imageUrl
               , this.content
               , this.solved
               , this.reporters
               , this.createdDate
               , this.image.withCollections(snsResponses, tagResponses, multipleImageResponses)
          );
     }
}
