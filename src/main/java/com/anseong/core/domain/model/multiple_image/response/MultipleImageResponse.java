package com.anseong.core.domain.model.multiple_image.response;

import java.util.Collection;
import java.util.List;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.response.ImageDetailResponse;
import com.anseong.core.domain.model.multiple_image.MultipleImage;
import com.anseong.core.domain.model.multiple_image.MultipleImageId;
import com.anseong.core.domain.model.user.UserId;

public record MultipleImageResponse(
     MultipleImageId id,
     ImageId imageId,
     UserId userId,
     int sequence,
     ImageDetailResponse detail
) implements Comparable<MultipleImageResponse> {

     public MultipleImageResponse(
          Long id,
          Long imageId,
          Long userId,
          int sequence,
          ImageDetailResponse detail
     ) {
          this(
               new MultipleImageId(id),
               new ImageId(imageId),
               new UserId(userId),
               sequence,
               detail
          );
     }

     public static MultipleImageResponse from(MultipleImage multipleImage) {
          return new MultipleImageResponse(
               multipleImage.getId(),
               multipleImage.getImageId(),
               multipleImage.getUserId(),
               multipleImage.getSequence(),
               ImageDetailResponse.from(multipleImage.getDetail())
          );
     }

     public static List<MultipleImageResponse> from(Collection<MultipleImage> multipleImages) {
          return multipleImages.stream()
               .map(MultipleImageResponse::from)
               .toList();
     }

     @Override
     public int compareTo(MultipleImageResponse o) {
          return sequence - o.sequence;
     }
}
