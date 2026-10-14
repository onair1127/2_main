package com.anseong.core.application.queries.store.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.anseong.core.application.queries.store.ResearchStoreGeoResponse;
import com.anseong.core.application.queries.store.services.StoreQueryRepositoryService;
import com.anseong.core.application.queries.store.services.StoreQueryService;
import com.anseong.core.domain.client.location.LocationClient;
import com.anseong.core.domain.client.location.request.AddressRequest;
import com.anseong.core.domain.model.store.repository.StoreCache;
import com.anseong.core.domain.model.store.response.SearchBoardStoreResponse;
import com.anseong.core.domain.model.store.response.SearchStoreResponse;
import com.anseong.core.domain.model.store.response.StoreForCertificateCardResponse;
import com.anseong.core.domain.model.store.response.StoreGeoResponse;
import com.anseong.core.domain.model.store.response.StoreResponse;
import com.anseong.core.domain.model.user.UserId;

@Service
class StoreQueryServiceImpl implements StoreQueryService {

     private final StoreQueryRepositoryService storeQueryRepositoryService;
     private final LocationClient locationClient;
     private final StoreCache storeCache;

     private final AsyncTaskExecutor virtualTaskExecutor;

     StoreQueryServiceImpl(
          StoreQueryRepositoryService storeQueryRepositoryService
          , LocationClient locationClient
          , StoreCache storeCache
          , AsyncTaskExecutor virtualTaskExecutor
     ) {
          this.storeQueryRepositoryService = storeQueryRepositoryService;
          this.locationClient = locationClient;
          this.storeCache = storeCache;
          this.virtualTaskExecutor = virtualTaskExecutor;
     }

     @Override
     public StoreResponse getStore(UserId userId) {
          Assert.notNull(userId, "가게 서비스 : 파라미터 `userId`가 비어있습니다.");

          StoreResponse response = storeQueryRepositoryService.findByUserId(userId);
          storeCache.incrementClickStoreCount(userId);

		return response;
	}

	@Override
     public List<SearchStoreResponse> search(String keyword, UserId userId, UserId lastStoreId, boolean isAdult) {
          Assert.hasText(keyword, "가게 서비스 : 파라미터 `keyword`가 비어있습니다.");

          List<SearchStoreResponse> responses = storeQueryRepositoryService.findByKeyword(keyword, lastStoreId, isAdult, 10);
          storeCache.addSearchHistory(userId, keyword);

          return responses;
     }

     @Override
     public List<SearchStoreResponse> getRandomSearchStores(boolean isAdult) {
          return storeQueryRepositoryService.findRandom(isAdult, 10);
     }

	@Override
     public StoreResponse getUpdatableStore(UserId userId) {
          Assert.notNull(userId, "가게 서비스 : 파라미터 `userId`가 비어있습니다.");

		return storeQueryRepositoryService.findByUserId(userId);
	}

     @Override
     public List<String> getStoreNames(String keyword, boolean isAdult) {
          return storeQueryRepositoryService.findStoreNames(keyword, isAdult, 10);
     }

     @Override
     public List<SearchBoardStoreResponse> getBoardStores(String storeName) {
          return storeQueryRepositoryService.findBoardStoresByName(storeName);
     }

     @Override
     public List<StoreGeoResponse> getStoreGeosByUserIds(List<UserId> geoStoreIds, List<String> tagLists, String current, boolean isAdult) {
          return storeQueryRepositoryService.findStoreGeosByUserIds(geoStoreIds, tagLists, current, isAdult);
     }

     @Override
     public StoreForCertificateCardResponse getStoreForCertificateCard(UserId userId) {
          Assert.notNull(userId, "가게 서비스 : 파라미터 `userId` 값이 null입니다.");

          return storeQueryRepositoryService.findStoreForCertificateCardByUserId(userId);
     }

     @Override
     public ResearchStoreGeoResponse researchStoresFromCoords(double lat, double lng, boolean isAdult) {
          CompletableFuture<ResearchStoreGeoResponse> storeGeoTask = CompletableFuture.supplyAsync(
               () -> {
                    List<UserId> storeIds = storeCache.getGeoStoreIds(lat, lng);
                    List<StoreGeoResponse> storeGeos = storeQueryRepositoryService.findStoreGeosByUserIds(storeIds, new ArrayList<>(), null, isAdult);

                    return new ResearchStoreGeoResponse(storeGeos, null);
               }
          , virtualTaskExecutor);

          CompletableFuture<String> addressTask = CompletableFuture.supplyAsync(
               () -> {
                    if (lat == 0 || lng == 0)
                         return null;
                    else
                         try {
                              AddressRequest request = AddressRequest.builder()
                                   .lat(lat)
                                   .lng(lng)
                                   .build();

                              return locationClient.getAddressByCoords(request);
                         } catch (Exception e) {
                              return null;
                         }
               }
          , virtualTaskExecutor);

          CompletableFuture<ResearchStoreGeoResponse> response = storeGeoTask
               .thenCombine(addressTask, (storeGeo, address) -> storeGeo.withAddress(address));

          return response.join();
     }
}
