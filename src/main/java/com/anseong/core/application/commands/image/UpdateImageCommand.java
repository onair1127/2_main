package com.anseong.core.application.commands.image;

import java.nio.charset.StandardCharsets;

import org.apache.catalina.util.URLEncoder;

import com.anseong.core.domain.model.image.ImageId;

public record UpdateImageCommand(
     ImageId imageId,
     String title,
     boolean job,
     int price,
     String description,
     boolean isSquare
) {
     public UpdateImageCommand(
          long imageId,
          String title,
          boolean job,
          int price,
          String description,
          boolean isSquare
     ) {
          this(
               new ImageId(imageId),
               URLEncoder.DEFAULT.encode(title, StandardCharsets.UTF_8),
               job,
               price,
               URLEncoder.DEFAULT.encode(description, StandardCharsets.UTF_8),
               isSquare
          );
     }
}
