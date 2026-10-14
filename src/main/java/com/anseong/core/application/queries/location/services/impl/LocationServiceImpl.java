package com.anseong.core.application.queries.location.services.impl;

import org.springframework.stereotype.Service;

import com.anseong.core.application.queries.location.services.LocationService;
import com.anseong.core.domain.client.location.LocationClient;
import com.anseong.core.domain.client.location.request.AddressRequest;
import com.anseong.core.domain.client.location.request.GeoCodeRequest;
import com.anseong.core.domain.client.location.response.GeoCodeResponse;

import io.jsonwebtoken.lang.Assert;

@Service
public class LocationServiceImpl implements LocationService {
     private final LocationClient locationClient;

	public LocationServiceImpl(LocationClient locationClient) {
          this.locationClient = locationClient;
     }

     @Override
     public GeoCodeResponse getCoordsByAddress(String address) {
          Assert.hasText(address, "주소 값이 비어있어 Geo 값을 불러올 수 없습니다.");

          GeoCodeRequest request = GeoCodeRequest.builder()
               .address(address)
               .build();

          return locationClient.getCoordsByAddress(request);
	}

     @Override
     public String getAddressByCoords(double lat, double lng) {
          Assert.notNull(lat, "파라미터 'lat'의 값은 필수입니다.");
          Assert.notNull(lng, "파라미터 'lng'의 값은 필수입니다.");

          AddressRequest request = AddressRequest.builder()
               .lat(lat)
               .lng(lng)
               .build();

          return locationClient.getAddressByCoords(request);
     }
}
