package com.anseong.core.domain.client.notificationn.request;

import com.anseong.core.domain.client.notificationn.NotificationPlatform;
import com.anseong.core.domain.model.user.UserId;

public record BatchImportFromAPNsTokenRequest(
     NotificationPlatform platform
     , UserId userId
     , String APNs
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private NotificationPlatform platform;
          private UserId userId;
          private String APNs;

          public Builder platform(NotificationPlatform platform) {
               this.platform = platform;
               return this;
          }

          public Builder userId(UserId userId) {
               this.userId = userId;
               return this;
          }

          public Builder APNs(String APNs) {
               this.APNs = APNs;
               return this;
          }

          public BatchImportFromAPNsTokenRequest build() {
               return new BatchImportFromAPNsTokenRequest(platform, userId, APNs);
          }
     }
}
