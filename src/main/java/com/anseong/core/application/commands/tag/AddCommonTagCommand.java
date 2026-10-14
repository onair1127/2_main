package com.anseong.core.application.commands.tag;

import java.util.List;

import com.anseong.core.domain.model.image.ImageId;

public record AddCommonTagCommand(
     String tagName,
     List<ImageId> imageIds
) {
     public static AddCommonTagCommand of(String tagName, List<Long> imageIds) {
          return new AddCommonTagCommand(
               tagName
               , imageIds.stream()
                    .map(ImageId::new)
                    .toList()
          );
     }
}
