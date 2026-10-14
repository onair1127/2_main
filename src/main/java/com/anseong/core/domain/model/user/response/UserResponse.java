package com.anseong.core.domain.model.user.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.anseong.core.domain.model.user.CeoId;
import com.anseong.core.domain.model.user.UserId;

public record UserResponse(
     UserId id,
     String username,
     String nickname,
     CeoId ceoId,
     String password,
     LocalDateTime createdDate,
     String customerUid,
     String merchantUid,
     String phone,
     boolean adult,
     boolean archived,
     boolean storeCreated,
     int birth,
     String profile,
     String logo
) {
     public UserResponse(
          long id,
          String username,
          String nickname,
          UUID ceoId,
          String password,
          LocalDateTime createdDate,
          String customerUid,
          String merchantUid,
          String phone,
          boolean adult,
          boolean archived,
          boolean storeCreated,
          int birth,
          String profile,
          String logo
     ) {
          this(
               new UserId(id),
               username,
               nickname,
               new CeoId(ceoId),
               password,
               createdDate,
               customerUid,
               merchantUid,
               phone,
               adult,
               archived,
               storeCreated,
               birth,
               profile,
               logo
          );
     }
}
