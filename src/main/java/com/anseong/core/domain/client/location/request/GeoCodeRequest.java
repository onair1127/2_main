package com.anseong.core.domain.client.location.request;

import com.anseong.core.domain.client.location.LocationPlatform;

public record GeoCodeRequest(
     LocationPlatform platform
     , String address
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private LocationPlatform platform;
          private String address;

          public Builder platform(LocationPlatform platform) {
               this.platform = platform;
               return this;
          }

          public Builder address(String address) {
               return this;
          }

          public GeoCodeRequest build() {
               return new GeoCodeRequest(platform, address);
          }
     }
}
