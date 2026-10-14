package com.anseong.core.application.queries.image;

import java.util.List;
import java.util.Map;

import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.store.response.StoreGeoResponse;

public record MainImageResponse(
     Map<String, String> randomRecommendTags,
     Map<String, List<ImageResponse>> randomTagImages,
     List<StoreGeoResponse> storeGeos,
     String address
) {
     public MainImageResponse withAddress(String address) {
          return new MainImageResponse(
               this.randomRecommendTags,
               this.randomTagImages,
               this.storeGeos,
               address
          );
     }

     public static Builder builder() {
          return new Builder();
     }

     public static Builder update(MainImageResponse mainImageResponse) {
          return new Builder(mainImageResponse);
     }

     public static class Builder {
          private Map<String, String> randomRecommendTags;
          private Map<String, List<ImageResponse>> randomTagImages;
          private List<StoreGeoResponse> storeGeos;
          private String address;

          private Builder() {}

          private Builder(MainImageResponse mainImageResponse) {
               this.randomRecommendTags = mainImageResponse.randomRecommendTags;
               this.randomTagImages = mainImageResponse.randomTagImages;
               this.storeGeos = mainImageResponse.storeGeos;
               this.address = mainImageResponse.address;
          }

          public Builder randomRecommendTags(Map<String, String> randomRecommendTags) {
               this.randomRecommendTags = randomRecommendTags;
               return this;
          }

          public Builder randomTagImages(Map<String, List<ImageResponse>> randomTagImages) {
               this.randomTagImages = randomTagImages;
               return this;
          }

          public Builder storeGeos(List<StoreGeoResponse> storeGeos) {
               this.storeGeos = storeGeos;
               return this;
          }

          public Builder address(String address) {
               this.address = address;
               return this;
          }

          public MainImageResponse build() {
               return new MainImageResponse(randomRecommendTags, randomTagImages, storeGeos, address);
          }
     }
}
