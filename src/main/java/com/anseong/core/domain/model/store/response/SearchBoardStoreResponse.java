package com.anseong.core.domain.model.store.response;

import com.anseong.core.domain.model.user.UserId;

public record SearchBoardStoreResponse(
     UserId userId,
     String storeName
) {
     public SearchBoardStoreResponse(long userId, String storeName) {
          this(
               new UserId(userId),
               storeName
          );
     }
}
