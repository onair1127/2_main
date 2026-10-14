package com.anseong.core.domain.model.store.response;

import java.util.List;
import java.util.Map;

import com.anseong.core.domain.model.store.OpeningHours;
import com.anseong.core.domain.model.tag.response.StoreTagResponse;
import com.anseong.core.domain.model.user.UserId;

public record SearchStoreResponse(
     UserId userId,
     String storeName,
     String address,
     String thumbnail,
     String poster,
     List<Integer> breakDates,
     int breakStart,
     int breakEnd,
     boolean adult,
     Map<String, String> openingHours,
     List<StoreTagResponse> tags
) {
     public SearchStoreResponse(
          long userId,
          String storeName,
          String address,
          String thumbnail,
          String poster,
          List<Integer> breakDates,
          int breakStart,
          int breakEnd,
          boolean adult,
          OpeningHours openingHours
     ) {
          this(
               new UserId(userId),
               storeName,
               address,
               thumbnail,
               poster,
               breakDates,
               breakStart,
               breakEnd,
               adult,
               openingHours.getAllOpeningHours(),
               null
          );
     }

     public SearchStoreResponse withTags(List<StoreTagResponse> tags) {
          return new SearchStoreResponse(
               this.userId
               , this.storeName
               , this.address
               , this.thumbnail
               , this.poster
               , this.breakDates
               , this.breakStart
               , this.breakEnd
               , this.adult
               , this.openingHours
               , tags
          );
     }
}
