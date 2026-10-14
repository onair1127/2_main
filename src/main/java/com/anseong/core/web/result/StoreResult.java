package com.anseong.core.web.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.store.response.StoreResponse;
import com.anseong.core.domain.model.tag.response.StoreTagResponse;

public class StoreResult {

     public static ResponseEntity<ApiResult> build(StoreResponse store) {
          List<Map<String, Object>> snsLists = new ArrayList<>();
          List<Map<String, Object>> tagLists = new ArrayList<>();
          ApiResult apiResult = ApiResult.blank();

          List<SnsResponse> snsSet = new ArrayList<>(store.sns());
          List<StoreTagResponse> tagSet = new ArrayList<>(store.tags());

          for (SnsResponse s : snsSet) {
               Map<String, Object> temp = new HashMap<>();
               temp.put("type", s.type());
               temp.put("url", s.url());

               snsLists.add(temp);
          }

          for (StoreTagResponse t : tagSet) {
               Map<String, Object> temp = new HashMap<>();

               temp.put("name", t.tagName());

               if (tagLists.contains(temp)) {
                    continue;
               }

               tagLists.add(temp);
          }

          apiResult
                    .add("id", store.userId().value())
                    .add("name", store.storeName())
                    .add("phone", store.phone())
                    .add("address", store.address())
                    .add("description", store.description())
                    .add("announcement", store.announcement())
                    .add("lat", store.lat())
                    .add("lng", store.lng())
                    .add("breakDates", store.breakDates())
                    .add("breakStart", store.breakStart())
                    .add("breakEnd", store.breakEnd())
                    .add("sns", snsLists)
                    .add("tags", tagLists)
                    .add("thumbnail", store.thumbnail())
                    .add("filter", store.filter())
                    .add("poster", store.poster())
                    .add("openingHours", store.openingHours())
                    .add("menuLists", store.menuLists());

          return Result.ok(apiResult);
     }
}
