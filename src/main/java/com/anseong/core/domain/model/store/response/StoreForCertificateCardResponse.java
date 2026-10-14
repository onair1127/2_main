package com.anseong.core.domain.model.store.response;

import com.anseong.core.domain.model.user.UserId;

public record StoreForCertificateCardResponse(
     UserId userId,
     String storeName,
     String phone,
     String address
) {
     public StoreForCertificateCardResponse(
          long userId,
          String storeName,
          String phone,
          String address
     ) {
          this(
               new UserId(userId),
               storeName,
               phone,
               address
          );
     }
}
