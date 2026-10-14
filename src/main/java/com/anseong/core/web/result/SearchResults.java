package com.anseong.core.web.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.springframework.http.ResponseEntity;

import com.anseong.core.domain.model.store.response.SearchStoreResponse;
import com.anseong.core.domain.model.tag.response.StoreTagResponse;

public class SearchResults {

     public static ResponseEntity<ApiResult> build(List<SearchStoreResponse> stores) {
          List<Map<String, Object>> results = new ArrayList<>();

          for (SearchStoreResponse store : stores) {
               Map<String, Object> temp = new HashMap<>();
               Set<Map<String, Object>> tagLists = new HashSet<>();

               for (StoreTagResponse tag : store.tags()) {
                    Map<String, Object> tagTemp = new HashMap<>();

                    tagTemp.put("name", tag.tagName());
                    tagLists.add(tagTemp);
               }

               temp.put("id", store.userId().value());
               temp.put("name", store.storeName());
               temp.put("address", store.address());
               temp.put("breakDates", store.breakDates());
               temp.put("breakStart", store.breakStart());
               temp.put("breakEnd", store.breakEnd());
               temp.put("thumbnail", store.thumbnail());
               temp.put("poster", store.poster());
               temp.put("openingHours", store.openingHours());
               temp.put("tags", tagLists);

               results.add(temp);
          }

          ApiResult apiResult = ApiResult.blank()
                    .add("results", results);
          return Result.ok(apiResult);
     }
}
