package com.anseong.core.application.queries.store.services;

import java.util.List;

import com.anseong.core.domain.model.store.response.SearchBoardStoreResponse;
import com.anseong.core.domain.model.store.response.SearchStoreResponse;
import com.anseong.core.domain.model.store.response.StoreForCertificateCardResponse;
import com.anseong.core.domain.model.store.response.StoreGeoResponse;
import com.anseong.core.domain.model.store.response.StoreResponse;
import com.anseong.core.domain.model.user.UserId;

public interface StoreQueryRepositoryService {
     StoreResponse findByUserId(UserId storeId);

     List<SearchStoreResponse> findByKeyword(String keyword, UserId lastUserId, boolean isAdult, int amount);

     List<SearchStoreResponse> findRandom(boolean isAdult, int amount);

     StoreForCertificateCardResponse findStoreForCertificateCardByUserId(UserId userId);

     List<String> findStoreNames(String keyword, boolean isAdult, int amount);

     List<SearchBoardStoreResponse> findBoardStoresByName(String storeName);

     List<StoreGeoResponse> findStoreGeosByUserIds(List<UserId> userIds, List<String> tagNames, String current, boolean isAdult);
}
