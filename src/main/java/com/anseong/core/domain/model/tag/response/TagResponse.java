package com.anseong.core.domain.model.tag.response;

import java.util.Collection;
import java.util.List;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.tag.Tag;
import com.anseong.core.domain.model.user.UserId;

public record TagResponse(
     ImageId imageId,
     UserId userId,
     String tagName,
     String type
) {
     public TagResponse(
          Long imageId,
          Long userId,
          String tagName,
          String type
     ) {
          this(
               new ImageId(imageId),
               new UserId(userId),
               tagName,
               type
          );
     }

     public static TagResponse from(Tag tag) {
          return new TagResponse(
               tag.getImageId(),
               tag.getUserId(),
               tag.getTagName(),
               tag.getType()
          );
     }

     public static List<TagResponse> from(Collection<Tag> tags) {
          return tags.stream()
               .map(TagResponse::from)
               .toList();
     }
}
