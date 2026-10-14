package com.anseong.core.application.commands.image;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.user.UserId;

public record DeleteImageCommand(
     ImageId imageId
     , UserId userId
     , boolean isArchived
     , String type
) {
     public DeleteImageCommand(
          long imageId
          , long userId
          , boolean isArchived
          , String type
     ) {
          this (
               new ImageId(imageId),
               new UserId(userId),
               isArchived,
               type
          );
     }
}
