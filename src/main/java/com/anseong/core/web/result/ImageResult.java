package com.anseong.core.web.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.multiple_image.response.MultipleImageResponse;
import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.store.response.ImageStoreResponse;
import com.anseong.core.domain.model.tag.response.TagResponse;

public class ImageResult {

     public static ResponseEntity<ApiResult> build(ImageResponse image) {
          List<String> tagNames = new ArrayList<>();
          List<Map<String, Object>> snsLists = new ArrayList<>();
          List<Map<String, Object>> multipleImages = new ArrayList<>();
          ImageStoreResponse store = image.store();

          for (TagResponse tag : image.tags()) {
               tagNames.add(tag.tagName());
          }

          for (SnsResponse sns : store.sns()) {
               Map<String, Object> snsTemp = new HashMap<>();
               snsTemp.put("type", sns.type());
               snsTemp.put("url", sns.url());
               snsLists.add(snsTemp);
          }

          if (image.multipleImages().size() != 0) {
               List<MultipleImageResponse> multipleImageSet = image.multipleImages();
               Collections.sort(multipleImageSet);

               for (MultipleImageResponse infoImage : multipleImageSet) {
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("thumbnail", infoImage.detail().thumbnail());
                    temp.put("card", infoImage.detail().card());
                    temp.put("filter", infoImage.detail().filter());
                    temp.put("isSquare", infoImage.detail().isSquare());
                    multipleImages.add(temp);
               }
          }

          ApiResult result = ApiResult.blank()
                              .add("id", image.id().value())
                              .add("thumbnail", image.detail().thumbnail())
                              .add("card", image.detail().card())
                              .add("title", image.title())
                              .add("price", image.price())
                              .add("description",
                                        !image.description().equals("")
                                        ? image.description()
                                        : store.description())
                              .add("storeId", store.userId().value())
                              .add("storeName", store.storeName())
                              .add("address", store.address())
                              .add("sns", snsLists)
                              .add("tags", tagNames)
                              .add("favorite", image.favorites())
                              .add("exposedCount", image.exposedCount())
                              .add("isSquare", image.detail().isSquare())
                              .add("type", image.type())
                              .add("filter", image.detail().filter())
                              .add("multipleImages", multipleImages);

          return Result.ok(result);
     }
}
