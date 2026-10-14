package com.anseong.core.web.result;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;

import com.anseong.core.domain.model.store.response.StoreGeoResponse;

public class StoreGeoResults {
     public static ResponseEntity<ApiResult> build(
               List<StoreGeoResponse> storeGeos,
               String address
     ) {
          ApiResult apiResult = ApiResult.blank();
          Map<String, Object> imageSet = new HashMap<>();
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
          apiResult.add("imageSet", imageSet);

          return Result.ok(apiResult);
     }
}
