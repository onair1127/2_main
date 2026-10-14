package com.anseong.core.domain.model.store.response;

import java.util.List;
import java.util.Map;

import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.store.ImageStore;
import com.anseong.core.domain.model.store.OpeningHours;
import com.anseong.core.domain.model.user.UserId;

public record ImageStoreResponse(
     UserId userId,
     String storeName,
     String address,
     String description,
     String thumbnail,
     boolean adult,
     String poster,
     String filter,
     Map<String, String> openingHours,
     List<SnsResponse> sns
) {
     public ImageStoreResponse(
          Long userId,
          String storeName,
          String address,
          String description,
          String thumbnail,
          boolean adult,
          String poster,
          String filter,
          OpeningHours openingHours
     ) {
          this(
               new UserId(userId),
               storeName,
               address,
               description,
               thumbnail,
               adult,
               poster,
               filter,
               openingHours.getAllOpeningHours(),
               null
          );
     }

     public static ImageStoreResponse from(ImageStore imageStore) {
          return new ImageStoreResponse(
               imageStore.getId(),
               imageStore.getStoreName(),
               imageStore.getAddress(),
               imageStore.getDescription(),
               imageStore.getThumbnail(),
               imageStore.isAdult(),
               imageStore.getPoster(),
               imageStore.getFilter(),
               imageStore.getOpeningHours().getAllOpeningHours(),
               SnsResponse.from(imageStore.getSns())
          );
     }

     public ImageStoreResponse withSns(List<SnsResponse> snsResponses) {
          return new ImageStoreResponse(
               this.userId,
               this.storeName,
               this.address,
               this.description,
               this.thumbnail,
               this.adult,
               this.poster,
               this.filter,
               this.openingHours,
               snsResponses
          );
     }
}
