package com.anseong.core.domain.model.sns.response;

import java.util.Collection;
import java.util.List;

import com.anseong.core.domain.model.sns.Sns;
import com.anseong.core.domain.model.user.UserId;

public record SnsResponse(
     UserId userId,
     String type,
     String url
) {
     public SnsResponse(
          Long userId,
          String type,
          String url
     ) {
          this(new UserId(userId), type, url);
     }

     public static SnsResponse from(Sns sns) {
          return new SnsResponse(
               sns.getUserId(),
               sns.getType(),
               sns.getUrl()
          );
     }

     public static List<SnsResponse> from(Collection<Sns> sns) {
          return sns.stream()
               .map(SnsResponse::from)
               .toList();
     }
}
