package com.anseong.core.application.commands.report;

import com.anseong.core.domain.model.image.ImageId;

public record ReportImageCommand(
     ImageId imageId
     , String imageUrl
     , String content
) {
     public ReportImageCommand(long imageId, String imageUrl, String content) {
          this(
               new ImageId(imageId)
               , imageUrl
               , content
          );
     }
}
