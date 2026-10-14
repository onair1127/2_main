package com.anseong.core.domain.client.messanger.request;

import com.anseong.core.domain.client.messanger.MessangerPlatform;
import com.anseong.core.domain.client.messanger.MessangerTemplate;
import com.anseong.core.domain.client.messanger.MessangerType;

public record MessangerRequest(
     MessangerPlatform platform,
     MessangerType type,
     MessangerTemplate template,
     String username
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private MessangerPlatform platform;
          private MessangerType type;
          private MessangerTemplate template;
          private String username;

          public Builder platform(MessangerPlatform platform) {
               this.platform = platform;
               return this;
          }

          public Builder type(MessangerType type) {
               this.type = type;
               return this;
          }

          public Builder template(MessangerTemplate template) {
               this.template = template;
               return this;
          }

          public Builder username(String username) {
               this.username = username;
               return this;
          }

          public MessangerRequest build() {
               return new MessangerRequest(platform, type, template, username);
          }
     }
}
