package com.anseong.core.web.result;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.store.response.StoreResponse;

public class UpdatableStoreResult {

     public static ResponseEntity<ApiResult> build(StoreResponse store) {

          List<String> snsUrls = new ArrayList<>();

          for (SnsResponse sns : store.sns()) {
               snsUrls.add(sns.url());
          }

          ApiResult result = ApiResult.blank()
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
                    .add("adult", store.adult())
                    .add("snsUrls", snsUrls)
                    .add("thumbnail", store.thumbnail())
                    .add("poster", store.poster())
                    .add("filter", store.filter())
                    .add("openingHours", store.openingHours())
                    .add("menuLists", store.menuLists());

          return Result.ok(result);
     }
}
