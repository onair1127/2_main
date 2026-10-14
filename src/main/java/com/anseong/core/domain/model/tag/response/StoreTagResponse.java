package com.anseong.core.domain.model.tag.response;

import java.util.Collection;
import java.util.List;

import com.anseong.core.domain.model.tag.StoreTag;
import com.anseong.core.domain.model.user.UserId;

public record StoreTagResponse(
     UserId userId,
     String tagName
) {
     public StoreTagResponse(
          long userId
          , String tagName
     ) {
          this(
               new UserId(userId),
               tagName
          );
     }

     public static StoreTagResponse from(StoreTag storeTag) {
          return new StoreTagResponse(
               storeTag.getUserId(),
               storeTag.getTagName()
          );
     }

     public static List<StoreTagResponse> from(Collection<StoreTag> storeTag) {
          return storeTag.stream()
               .map(StoreTagResponse::from)
               .toList();
     }
}
