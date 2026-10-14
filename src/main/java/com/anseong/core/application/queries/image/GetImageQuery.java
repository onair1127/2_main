package com.anseong.core.application.queries.image;

import java.util.List;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.user.UserId;

public record GetImageQuery(
     List<String> tagLists
     , List<ImageId> imageIds
     , List<UserId> storeIds
     , String current
     , boolean job
     , double lat
     , double lng
) {
     public static GetImageQuery of(
          List<String> tagLists
          , List<Long> imageIds
          , List<Long> storeIds
          , String current
          , boolean job
          , double lat
          , double lng
     ) {
          return new GetImageQuery(
               tagLists
               , imageIds.stream().map(ImageId::new).toList()
               , storeIds.stream().map(UserId::new).toList()
               , current
               , job
               , lat
               , lng
          );
     }
}
