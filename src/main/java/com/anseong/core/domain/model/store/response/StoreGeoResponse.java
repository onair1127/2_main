package com.anseong.core.domain.model.store.response;

import java.util.Collection;
import java.util.List;

import com.anseong.core.domain.model.store.StoreGeo;
import com.anseong.core.domain.model.user.UserId;

public record StoreGeoResponse(
     UserId userId,
     String storeName,
     String thumbnail,
     String filter,
     String poster,
     double lat,
     double lng
) {
     public StoreGeoResponse(
          Long userId,
          String storeName,
          String thumbnail,
          String filter,
          String poster,
          double lat,
          double lng
     ) {
          this(
               new UserId(userId),
               storeName,
               thumbnail,
               filter,
               poster,
               lat,
               lng
          );
     }

     public static StoreGeoResponse from(StoreGeo storeGeo) {
          return new StoreGeoResponse(
               storeGeo.getUserId(),
               storeGeo.getStoreName(),
               storeGeo.getThumbnail(),
               storeGeo.getFilter(),
               storeGeo.getPoster(),
               storeGeo.getLat(),
               storeGeo.getLng()
          );
     }

     public static List<StoreGeoResponse> from(Collection<StoreGeo> storeGeos) {
          return storeGeos.stream()
               .map(StoreGeoResponse::from)
               .toList();
     }
}
