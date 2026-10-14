package com.anseong.core.application.queries.location.services;

import com.anseong.core.domain.client.location.response.GeoCodeResponse;

public interface LocationService {
     GeoCodeResponse getCoordsByAddress(String address);

     String getAddressByCoords(double lat, double lng);
}
