package com.anseong.core.application.queries.image;

import java.util.List;

import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.store.response.StoreGeoResponse;

public record GetImageResponse(
     List<ImageResponse> images,
     List<StoreGeoResponse> storeGeoResponses,
     String address
) {
     public GetImageResponse withAddress(String address) {
          return new GetImageResponse(this.images, this.storeGeoResponses, address);
     }

     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private List<ImageResponse> images;
          private List<StoreGeoResponse> storeGeoResponses;
          private String address;

          public Builder images(List<ImageResponse> images) {
               this.images = images;
               return this;
          }

          public Builder storeGeoResponses(List<StoreGeoResponse> storeGeoResponses) {
               this.storeGeoResponses = storeGeoResponses;
               return this;
          }

          public Builder address(String address) {
               this.address = address;
               return this;
          }

          public GetImageResponse build() {
               return new GetImageResponse(images, storeGeoResponses, address);
          }
     }
}
