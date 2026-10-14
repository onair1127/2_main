package com.anseong.core.domain.client.convert.media;

public enum ConvertMediaMode {
     BOARD("board"),
     PROFILE("profile"),
     IMAGE("image"),
     USER_PROFILE("userProfile");

     private final String value;

     ConvertMediaMode(String value) {
          this.value = value;
     }

     public String value() {
          return value;
     }
}
