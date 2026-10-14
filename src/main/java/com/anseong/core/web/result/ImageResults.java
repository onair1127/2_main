package com.anseong.core.web.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;

import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.multiple_image.response.MultipleImageResponse;
import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.store.response.ImageStoreResponse;
import com.anseong.core.domain.model.store.response.StoreGeoResponse;
import com.anseong.core.domain.model.tag.response.TagResponse;

public class ImageResults {

     /**
      * 내부 클래스 활용해서 리턴하는 방법도 있는데
      * 노란줄 너무 신경쓰여서 삽질로 간다 ^^
      *
      * @param archivedImages
      * @param favorites
      * @return
      */
     public static ResponseEntity<ApiResult> build(
               List<ImageResponse> images,
               String address,
               List<StoreGeoResponse> storeGeos,
               boolean hasStoreData,
               boolean isReview
     ) {
          List<Map<String, Object>> results = new ArrayList<>();
          List<String> tagNames;                       List<TagResponse> tagSet;
          List<Map<String, Object>> multipleImages;    List<MultipleImageResponse> multipleImageSet;
          Set<Map<String, Object>> storeLocations = null;

          for (ImageResponse image : images) {
               Map<String, Object> temp = new HashMap<>();
               List<Map<String, Object>> snsLists = new ArrayList<>();
               long id = image.id().value();
               ImageStoreResponse store = image.store();

               tagNames = new ArrayList<>();
               tagSet = image.tags();
               multipleImages = new ArrayList<>();
               multipleImageSet = image.multipleImages();

               Collections.sort(multipleImageSet);

               for (TagResponse tag : tagSet) {
                    tagNames.add(tag.tagName());
               }

               for (SnsResponse sns : store.sns()) {
                    Map<String, Object> snsTemp = new HashMap<>();
                    snsTemp.put("type", sns.type());
                    snsTemp.put("url", sns.url());
                    snsLists.add(snsTemp);
               }

               if (multipleImageSet.size() != 0) {
                    for (int i = 0; i < multipleImageSet.size(); i++) {
                         if (multipleImageSet.size() == i)
                              break;

                         Map<String, Object> tmp = new LinkedHashMap<>();
                         tmp.put("id", multipleImageSet.get(i).id().value());
                         tmp.put("thumbnail", multipleImageSet.get(i).detail().thumbnail());
                         tmp.put("card", multipleImageSet.get(i).detail().card());
                         tmp.put("isSquare", multipleImageSet.get(i).detail().isSquare());
                         tmp.put("filter", multipleImageSet.get(i).detail().filter());
                         multipleImages.add(tmp);
                    }
               }

               temp.put("id", id);
               temp.put("storeId", image.storeId().value());
               temp.put("thumbnail", image.detail().thumbnail());
               temp.put("card", image.detail().card());
               temp.put("title", image.title());
               temp.put("price", image.price());
               temp.put("sns", snsLists);

               if (hasStoreData) {
                    temp.put("storeName", store.storeName());
                    temp.put("address", store.address());
                    temp.put("storeThumbnail", store.thumbnail());
                    temp.put("storeThumbnailFilter", store.filter());
                    temp.put("storePoster", store.poster());
                    temp.put("description",
                         !image.description().equals("")
                         ? image.description()
                         : store.description());
               } else {
                    temp.put("description", image.description());
               }

               temp.put("tags", tagNames);
               temp.put("favorite", image.favorites());
               temp.put("exposedCount", image.exposedCount());
               temp.put("type", image.type());
               temp.put("filter", image.detail().filter());
               temp.put("isSquare", image.detail().isSquare());
               temp.put("createdDate", image.detail().createdDate());
               temp.put("multipleImages", multipleImages);
               temp.put("userNickname", image.user().nickname());
               temp.put("userProfile", image.user().profile());
               results.add(temp);
          }

          if (storeGeos != null) {
               storeLocations = new HashSet<>();

               for (StoreGeoResponse store : storeGeos) {
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("id", store.userId().value());
                    temp.put("name", store.storeName());
                    temp.put("thumbnail", store.thumbnail());
                    temp.put("poster", store.poster());
                    temp.put("filter", store.filter());
                    temp.put("lat", store.lat());
                    temp.put("lng", store.lng());

                    storeLocations.add(temp);
               }
          }

          ApiResult apiResult = ApiResult.blank()
               .add("images", results)
               .add("address", address != null ? address : "")
               .add("storeLocations", storeLocations);

          return Result.ok(apiResult);
     }
}
