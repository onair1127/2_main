package com.anseong.core.domain.model.image.response;

import java.time.LocalDateTime;

import com.anseong.core.domain.model.image.ImageDetail;

public record ImageDetailResponse(
     String filename,
     String thumbnail,
     String card,
     String filter,
     boolean isSquare,
     boolean archived,
     LocalDateTime createdDate
) {
     public static ImageDetailResponse from(ImageDetail imageDetail) {
          return new ImageDetailResponse(
               imageDetail.getFilename(),
               imageDetail.getThumbnail(),
               imageDetail.getCard(),
               imageDetail.getFilter(),
               imageDetail.isSquare(),
               imageDetail.isArchived(),
               imageDetail.getCreatedDate()
          );
     }
}
