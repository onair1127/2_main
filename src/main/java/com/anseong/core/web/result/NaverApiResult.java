package com.anseong.core.web.result;

import org.springframework.http.ResponseEntity;

import com.anseong.core.domain.client.location.response.GeoCodeResponse;

public class NaverApiResult {

     public static ResponseEntity<ApiResult> geoBuild(GeoCodeResponse response) {
          ApiResult apiResult = ApiResult.blank()
                    .add("lat", response.lat())
                    .add("lng", response.lng());

          return Result.ok(apiResult);
     }
}
