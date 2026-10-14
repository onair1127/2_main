package com.anseong.core.domain.client.location;

import com.anseong.core.domain.client.location.request.AddressRequest;
import com.anseong.core.domain.client.location.request.GeoCodeRequest;
import com.anseong.core.domain.client.location.response.GeoCodeResponse;

public interface LocationClient {
     GeoCodeResponse getCoordsByAddress(GeoCodeRequest request);

     String getAddressByCoords(AddressRequest request);
}
