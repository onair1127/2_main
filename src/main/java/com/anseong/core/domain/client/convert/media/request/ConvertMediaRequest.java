package com.anseong.core.domain.client.convert.media.request;

import com.anseong.core.domain.client.convert.media.ConvertMediaPlatform;

public record ConvertMediaRequest(
     ConvertMediaPlatform platform,
     String url,
     String ext,
     String filename,
     String username,
     int sequence,
     boolean isSquare,
     ConvertMode mode
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private ConvertMediaPlatform platform;
          private String url;
          private String ext;
          private String filename;
          private String username;
          private int sequence;
          private boolean isSquare;
          private ConvertMode convertMode;

          public Builder platform(ConvertMediaPlatform platform) {
               this.platform = platform;
               return this;
          }

          public Builder url(String url) {
               this.url = url;
               return this;
          }

          public Builder ext(String ext) {
               this.ext = ext;
               return this;
          }

          public Builder filename(String filename) {
               this.filename = filename;
               return this;
          }

          public Builder username(String username) {
               this.username = username;
               return this;
          }

          public Builder sequence(int sequence) {
               this.sequence = sequence;
               return this;
          }

          public Builder isSquare(boolean isSquare) {
               this.isSquare = isSquare;
               return this;
          }

          public Builder convertMode(ConvertMode convertMode) {
               this.convertMode = convertMode;
               return this;
          }

          public ConvertMediaRequest build() {
               return new ConvertMediaRequest(platform, url, ext, filename, username, sequence, isSquare, convertMode);
          }
     }

     public enum ConvertMode {
          BOARD("board"),
          PROFILE("profile"),
          IMAGE("image"),
          USER_PROFILE("userProfile");

          private final String value;

          ConvertMode(String value) {
               this.value = value;
          }

          public String value() {
               return value;
          }
     }
}
