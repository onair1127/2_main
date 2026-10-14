package com.anseong.core.application.queries.store.services;

import java.util.List;

import com.anseong.core.application.queries.store.ResearchStoreGeoResponse;
import com.anseong.core.domain.model.store.response.SearchBoardStoreResponse;
import com.anseong.core.domain.model.store.response.SearchStoreResponse;
import com.anseong.core.domain.model.store.response.StoreForCertificateCardResponse;
import com.anseong.core.domain.model.store.response.StoreGeoResponse;
import com.anseong.core.domain.model.store.response.StoreResponse;
import com.anseong.core.domain.model.user.UserId;

public interface StoreQueryService {
     StoreResponse getStore(UserId userId);

	List<SearchStoreResponse> search(String keyword, UserId userId, UserId lastStoreId, boolean isAdult);

	List<SearchStoreResponse> getRandomSearchStores(boolean isAdult);

	StoreResponse getUpdatableStore(UserId storeId);

     List<String> getStoreNames(String keyword, boolean adult);

     List<SearchBoardStoreResponse> getBoardStores(String storeName);

     List<StoreGeoResponse> getStoreGeosByUserIds(List<UserId> geoStoreIds, List<String> tagLists, String current, boolean isAdult);

     StoreForCertificateCardResponse getStoreForCertificateCard(UserId userId);

     ResearchStoreGeoResponse researchStoresFromCoords(double lat, double lng, boolean isAdult);
}
