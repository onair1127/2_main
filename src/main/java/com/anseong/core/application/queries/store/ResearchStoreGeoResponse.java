package com.anseong.core.application.queries.store;

import java.util.List;

import com.anseong.core.domain.model.store.response.StoreGeoResponse;

public record ResearchStoreGeoResponse(
     List<StoreGeoResponse> storeGeoResponses,
     String address
) {
     public ResearchStoreGeoResponse withAddress(String address) {
          return new ResearchStoreGeoResponse(this.storeGeoResponses, address);
     }
}
