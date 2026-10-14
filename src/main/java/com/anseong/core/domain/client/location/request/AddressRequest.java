package com.anseong.core.domain.client.location.request;

import com.anseong.core.domain.client.location.LocationPlatform;

public record AddressRequest(
     LocationPlatform platform
     , double lat
     , double lng
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private LocationPlatform platform;
          private double lat;
          private double lng;

          public Builder platform(LocationPlatform platform) {
               this.platform = platform;
               return this;
          }

          public Builder lat(double lat) {
               this.lat = lat;
               return this;
          }

          public Builder lng(double lng) {
               this.lng = lng;
               return this;
          }

          public AddressRequest build() {
               return new AddressRequest(platform, lat, lng);
          }
     }
}
