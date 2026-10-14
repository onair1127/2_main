package com.anseong.core.domain.model.store.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.store.OpeningHours;
import com.anseong.core.domain.model.store.Store;
import com.anseong.core.domain.model.tag.response.StoreTagResponse;
import com.anseong.core.domain.model.user.UserId;

public record StoreResponse(
     UserId userId,
     String storeName,
     long bizNo,
     String phone,
     String address,
     String description,
     String announcement,
     double lat,
     double lng,
     String thumbnail,
     LocalDateTime createdDate,
     List<Integer> breakDates,
     int breakStart,
     int breakEnd,
     boolean adult,
     String filter,
     String poster,
     String menuLists,
     Map<String, String> openingHours,
     List<SnsResponse> sns,
     List<StoreTagResponse> tags
) {
     public StoreResponse(
          Long userId
          , String storeName
          , Long bizNo
          , String phone
          , String address
          , String description
          , String announcement
          , double lat
          , double lng
          , String thumbnail
          , LocalDateTime createdDate
          , List<Integer> breakDates
          , int breakStart
          , int breakEnd
          , boolean adult
          , String filter
          , String poster
          , String menuLists
          , OpeningHours openingHours
     ) {
          this(
               new UserId(userId)
               , storeName
               , bizNo
               , phone
               , address
               , description
               , announcement
               , lat
               , lng
               , thumbnail
               , createdDate
               , breakDates
               , breakStart
               , breakEnd
               , adult
               , filter
               , poster
               , menuLists
               , openingHours.getAllOpeningHours()
               , null
               , null
          );
     }

     public static StoreResponse from(Store store) {
          return new StoreResponse(
               store.getUserId(),
               store.getStoreName(),
               store.getBizNo(),
               store.getPhone(),
               store.getAddress(),
               store.getDescription(),
               store.getAnnouncement(),
               store.getLat(),
               store.getLng(),
               store.getThumbnail(),
               store.getCreatedDate(),
               store.getBreakDates(),
               store.getBreakStart(),
               store.getBreakEnd(),
               store.isAdult(),
               store.getFilter(),
               store.getPoster(),
               store.getMenuLists(),
               store.getOpeningHours().getAllOpeningHours(),
               SnsResponse.from(store.getSns()),
               StoreTagResponse.from(store.getTags())
          );
     }

     public StoreResponse withSnsAndTags(List<SnsResponse> sns, List<StoreTagResponse> tags) {
          return new StoreResponse(
               this.userId
               , this.storeName
               , this.bizNo
               , this.phone
               , this.address
               , this.description
               , this.announcement
               , this.lat
               , this.lng
               , this.thumbnail
               , this.createdDate
               , this.breakDates
               , this.breakStart
               , this.breakEnd
               , this.adult
               , this.filter
               , this.poster
               , this.menuLists
               , this.openingHours
               , sns
               , tags
          );
     }
}
