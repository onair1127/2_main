package com.anseong.core.application.commands.image;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.catalina.util.URLEncoder;

import com.anseong.core.application.commands.tag.TagCommand;
import com.anseong.core.domain.model.user.UserId;

public record UploadImageCommand(
     List<ImageCommand> images,
     List<TagCommand> tags,
     UserId storeId,
     String type,
     int price,
     String title,
     String description,
     String phone,
     boolean isJob,
     LocalDateTime createdDate
) {
     public UploadImageCommand(
          List<Map<String, Object>> images
          , List<Map<String, String>> tags
          , long storeId
          , String type
          , int price
          , String title
          , String description
          , String phone
          , boolean isJob
          , LocalDateTime createdDate
     ) {
          this(
               images.stream()
                    .map(image -> new ImageCommand(
                         String.valueOf(image.get("url"))
                         , URLEncoder.DEFAULT.encode(String.valueOf(image.get("filename")), StandardCharsets.UTF_8)
                         , String.valueOf(image.get("ext"))
                         , String.valueOf(image.get("filter"))
                         , (int) (image.get("sequence"))
                         , (boolean) image.get("isSquare")
                    ))
                    .toList()

               , tags.stream()
                    .map(tag -> new TagCommand(
                         String.valueOf(tag.get("name"))
                         , String.valueOf(tag.get("type"))
                    ))
                    .toList()

               , new UserId(storeId)
               , type
               , price
               , URLEncoder.DEFAULT.encode(title, StandardCharsets.UTF_8)
               , URLEncoder.DEFAULT.encode(description, StandardCharsets.UTF_8)
               , phone
               , isJob
               , createdDate
          );
     }
}
