package com.anseong.core.domain.client.notificationn.request;

import com.anseong.core.domain.client.notificationn.NotificationPlatform;
import com.anseong.core.domain.client.notificationn.NotificationTopic;
import com.anseong.core.domain.model.user.UserId;

public record SubscribeRequest(
     NotificationPlatform platform
     , NotificationTopic topic
     , UserId userId
     , String token
     , boolean isCeo
     , boolean isAdult
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private NotificationPlatform platform;
          private NotificationTopic topic;
          private UserId userId;
          private String token;
          private boolean isCeo;
          private boolean isAdult;

          public Builder platform(NotificationPlatform platform) {
               this.platform = platform;
               return this;
          }

          public Builder topic(NotificationTopic topic) {
               this.topic = topic;
               return this;
          }

          public Builder userId(UserId userId) {
               this.userId = userId;
               return this;
          }

          public Builder token(String token) {
               this.token = token;
               return this;
          }

          public Builder isCeo(boolean isCeo) {
               this.isCeo = isCeo;
               return this;
          }

          public Builder isAdult(boolean isAdult) {
               this.isAdult = isAdult;
               return this;
          }

          public SubscribeRequest build() {
               return new SubscribeRequest(platform, topic, userId, token, isCeo, isAdult);
          }
     }
}
