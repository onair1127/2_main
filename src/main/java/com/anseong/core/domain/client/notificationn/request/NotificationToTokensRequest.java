package com.anseong.core.domain.client.notificationn.request;

import java.util.Set;

import com.anseong.core.domain.client.notificationn.NotificationPlatform;
import com.anseong.core.domain.model.user.UserId;

public record NotificationToTokensRequest(
     NotificationPlatform platform
     , UserId userId
     , Set<String> tokens
     , String thumbnail
     , String redirect
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private NotificationPlatform platform;
          private UserId userId;
          private Set<String> tokens;
          private String thumbnail;
          private String redirect;

          public Builder platform(NotificationPlatform platform) {
               this.platform = platform;
               return this;
          }

          public Builder userId(UserId userId) {
               this.userId = userId;
               return this;
          }

          public Builder tokens(Set<String> tokens) {
               this.tokens = tokens;
               return this;
          }

          public Builder thumbnail(String thumbnail) {
               this.thumbnail = thumbnail;
               return this;
          }

          public Builder redirect(String redirect) {
               this.redirect = redirect;
               return this;
          }

          public NotificationToTokensRequest build() {
               return new NotificationToTokensRequest(platform, userId, tokens, thumbnail, redirect);
          }
     }
}
