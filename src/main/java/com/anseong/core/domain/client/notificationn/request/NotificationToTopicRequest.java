package com.anseong.core.domain.client.notificationn.request;

import com.anseong.core.domain.client.notificationn.NotificationPlatform;
import com.anseong.core.domain.client.notificationn.NotificationTopic;
import com.anseong.core.domain.model.user.UserId;

public record NotificationToTopicRequest(
     NotificationPlatform platform
     , NotificationTopic topic
     , UserId userId
     , String description
     , String title
     , String thumbnail
     , String redirect
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private NotificationPlatform platform;
          private NotificationTopic topic;
          private UserId userId;
          private String description;
          private String title;
          private String thumbnail;
          private String redirect;

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

          public Builder description(String description) {
               this.description = description;
               return this;
          }

          public Builder title(String title) {
               this.title = title;
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

          public NotificationToTopicRequest build() {
               return new NotificationToTopicRequest(platform, topic, userId, description, title, thumbnail, redirect);
          }
     }
}
