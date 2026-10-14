package com.anseong.core.infrastructure.client.location.naver;

import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.anseong.core.domain.client.location.LocationPlatform;
import com.anseong.core.domain.client.location.request.AddressRequest;
import com.anseong.core.domain.client.location.request.GeoCodeRequest;
import com.anseong.core.domain.client.location.response.GeoCodeResponse;
import com.anseong.core.infrastructure.client.location.InternalLocationClient;
import com.anseong.core.infrastructure.client.location.naver.ResponseBody.Geocoding;
import com.anseong.core.infrastructure.client.location.naver.ResponseBody.ReverseGeocoding;
import com.anseong.global.utils.JsonUtils;

@Component
class NaverLocationClient implements InternalLocationClient {

     private final RestClient naverStaticRestClient;

     NaverLocationClient(@Qualifier("naverStaticRestClient") RestClient.Builder naverStaticRestClient) {
          this.naverStaticRestClient = naverStaticRestClient.build();
     }

     @Override
     public GeoCodeResponse getCoordsByAddress(GeoCodeRequest request) {
          Geocoding responseBody = naverStaticRestClient.get()
               .uri("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(request.address(), Charset.forName("UTF-8")))
               .retrieve()
               .onStatus(HttpStatusCode::isError, (req, res) -> {
                    ResponseBody.Geocoding errorResponse = JsonUtils.toObject(res.getBody(), ResponseBody.Geocoding.class);
                    String errorMessage = errorResponse != null ? errorResponse.errorMessage() : "알 수 없는 에러 : " + res.getStatusText();

                    throw new NaverLocationLoadFailedException(NaverLocationErrorType.GEOCODE_LOAD_FAILED, res.getStatusCode(), errorMessage);
               })
               .body(ResponseBody.Geocoding.class);

          if (responseBody == null)
               throw new NaverLocationLoadFailedException(NaverLocationErrorType.GEOCODE_LOAD_FAILED, HttpStatus.ACCEPTED, "응답 바디가 비어있음.");

          return responseBody.toDomainResponse();
     }

     @Override
     public String getAddressByCoords(AddressRequest request) {
          ReverseGeocoding responseBody = naverStaticRestClient.get()
               .uri("https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?output=json&coords=" + request.lng() + "," + request.lat())
               .retrieve()
               .onStatus(HttpStatusCode::isError, (req, res) -> {
                    ResponseBody.ReverseGeocoding errorResponse = JsonUtils.toObject(res.getBody(), ResponseBody.ReverseGeocoding.class);
                    String errorMessage = errorResponse != null ? errorResponse.status().name() + " : " + errorResponse.status().message() : "알 수 없는 에러 : " + res.getStatusText();

                    throw new NaverLocationLoadFailedException(NaverLocationErrorType.REVERSE_GEOCODE_LOAD_FAILED, res.getStatusCode(), errorMessage);
               })
               .body(ResponseBody.ReverseGeocoding.class);

          if (responseBody == null)
               throw new NaverLocationLoadFailedException(NaverLocationErrorType.REVERSE_GEOCODE_LOAD_FAILED, HttpStatus.ACCEPTED, "응답 바디가 비어있음.");

          return responseBody.toDomainResponse();
     }

     @Override
     public LocationPlatform supports() {
          return LocationPlatform.NAVER;
     }
}
