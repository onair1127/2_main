package com.anseong.core.application.commands.tag;

import com.anseong.core.domain.model.image.ImageId;

public record AddPersonalTagCommand(
     ImageId imageId,
     String tagName
) {
     public AddPersonalTagCommand(long imageId, String tagName) {
          this(
               new ImageId(imageId),
               tagName
          );
     }
}
