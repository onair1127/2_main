package com.anseong.core.infrastructure.client.location;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import com.anseong.core.domain.client.location.LocationClient;
import com.anseong.core.domain.client.location.LocationPlatform;
import com.anseong.core.domain.client.location.exception.LocationLoadFailedException;
import com.anseong.core.domain.client.location.request.AddressRequest;
import com.anseong.core.domain.client.location.request.GeoCodeRequest;
import com.anseong.core.domain.client.location.response.GeoCodeResponse;

import io.github.resilience4j.retry.annotation.Retry;

@Component
@Primary
@Retry(name = "location")
class LocationClientFactory implements LocationClient {

     private final Map<LocationPlatform, LocationClient> clients;
     private final LocationPlatform DEFAULT_PLATFORM = LocationPlatform.NAVER;

     LocationClientFactory(List<InternalLocationClient> locationClients) {
          this.clients = locationClients.stream()
               .collect(Collectors.toMap(InternalLocationClient::supports, Function.identity()));
     }

     private LocationClient getClient(LocationPlatform platform) {
          if (platform == null)
               platform = DEFAULT_PLATFORM;

          return clients.get(platform);
     }

     @Override
     public GeoCodeResponse getCoordsByAddress(GeoCodeRequest request) throws LocationLoadFailedException {
          return getClient(request.platform()).getCoordsByAddress(request);
     }

     @Override
     public String getAddressByCoords(AddressRequest request) throws LocationLoadFailedException {
          return getClient(request.platform()).getAddressByCoords(request);
     }
}
