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

public class RandomTagsAndImagesResults {

     public static ResponseEntity<ApiResult> build(
               Map<String, String> randomRecommendTags,
               Map<String, List<ImageResponse>> randomTagImages,
               List<StoreGeoResponse> storeGeos,
               String address
     ) {
          ApiResult apiResult = ApiResult.blank();
          Map<String, Object> imageSet = new HashMap<>();
          List<String> randomRecommendTagKeys = new ArrayList<>(randomRecommendTags.keySet());
          List<String> tagNames;                       List<TagResponse> tagSet;
          List<Map<String, Object>> multipleImages;   List<MultipleImageResponse> multipleImageSet;

          List<String> imageTagSet = new ArrayList<>(randomTagImages.keySet());
          for (int index = 0; index < randomTagImages.keySet().size(); index++) {

               List<Map<String, Object>> imageResults = new ArrayList<>();
               String tag = imageTagSet.get(index);

               for (ImageResponse image : randomTagImages.get(tag)) {
                    Map<String, Object> temp = new HashMap<>();
                    List<Map<String, Object>> snsLists = new ArrayList<>();
                    ImageStoreResponse store = image.store();

                    tagNames = new ArrayList<>();
                    tagSet = new ArrayList<>(image.tags());
                    multipleImages = new ArrayList<>();
                    multipleImageSet = new ArrayList<>(image.multipleImages());

                    Collections.sort(multipleImageSet);

                    for (TagResponse t : tagSet) {
                         tagNames.add(t.tagName());
                    }

                    for (SnsResponse s : store.sns()) {
                         Map<String, Object> snsTemp = new HashMap<>();
                         snsTemp.put("type", s.type());
                         snsTemp.put("url", s.url());
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

                    temp.put("id", image.id().value());
                    temp.put("storeId", image.storeId().value());
                    temp.put("thumbnail", image.detail().thumbnail());
                    temp.put("card", image.detail().card());
                    temp.put("title", image.title());
                    temp.put("price", image.price());
                    temp.put("description", image.description().isBlank() ? store.description() : image.description());
                    temp.put("tags", tagNames);
                    temp.put("favorite", image.favorites());
                    temp.put("exposedCount", image.exposedCount());
                    temp.put("type", image.type());
                    temp.put("filter", image.detail().filter());
                    temp.put("isSquare", image.detail().isSquare());
                    temp.put("multipleImages", multipleImages);
                    temp.put("storeName", store.storeName());
                    temp.put("storeThumbnail", store.thumbnail());
                    temp.put("storeThumbnailFilter", store.filter());
                    temp.put("storePoster", store.poster());
                    temp.put("address", store.address());
                    temp.put("sns", snsLists);
                    temp.put("createdDate", image.detail().createdDate());
                    temp.put("userNickname", image.user().nickname());
                    temp.put("userProfile", image.user().profile());
                    imageResults.add(temp);
               }

               Map<String, Object> indexResults = new HashMap<>();
               Map<String, String> recommendTag = new HashMap<>();
               String recommendTagKey = randomRecommendTagKeys.get(index);

               recommendTag.put(recommendTagKey, randomRecommendTags.get(recommendTagKey));
               indexResults.put("recommendTag", recommendTag);
               indexResults.put("images", imageResults);
               imageSet.put(String.valueOf(index), indexResults);
          }

          if (storeGeos != null) {
               Set<Map<String, Object>> storeLocations = new HashSet<>();

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

               apiResult.add("storeLocations", storeLocations);
               apiResult.add("address", address);
          }

          apiResult.add("imageSet", imageSet);

          return Result.ok(apiResult);
     }
}
