package com.anseong.core.web.apis;

import com.anseong.core.domain.client.location.LocationClient;
import com.anseong.core.domain.client.location.request.GeoCodeRequest;
import com.anseong.core.domain.client.location.response.GeoCodeResponse;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.web.apis.authenticate.CurrentUser;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.result.NaverApiResult;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ExternalApiController {

     private final LocationClient locationClient;

     ExternalApiController(LocationClient locationClient) {
          this.locationClient = locationClient;
     }

     @GetMapping("/api/geo/{address}")
     ResponseEntity<ApiResult> getGeoCode(
               @PathVariable("address") String address,
               @CurrentUser SimpleUser user
     ) {
          GeoCodeRequest request = GeoCodeRequest.builder()
               .address(address)
               .build();

          GeoCodeResponse response = locationClient.getCoordsByAddress(request);
          return NaverApiResult.geoBuild(response);
     }
}
