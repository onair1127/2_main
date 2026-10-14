package com.anseong.core.application.queries.store.services.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.queries.store.services.StoreQueryRepositoryService;
import com.anseong.core.domain.aop.cache.ResponseCacheable;
import com.anseong.core.domain.aop.semaphore.ReplicaDatasourceSemaphore;
import com.anseong.core.domain.model.sns.repository.SnsQueryRepository;
import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.store.repository.StoreQueryRepository;
import com.anseong.core.domain.model.store.response.SearchBoardStoreResponse;
import com.anseong.core.domain.model.store.response.SearchStoreResponse;
import com.anseong.core.domain.model.store.response.StoreForCertificateCardResponse;
import com.anseong.core.domain.model.store.response.StoreGeoResponse;
import com.anseong.core.domain.model.store.response.StoreResponse;
import com.anseong.core.domain.model.tag.repository.TagQueryRepository;
import com.anseong.core.domain.model.tag.response.StoreTagResponse;
import com.anseong.core.domain.model.user.UserId;

@Service
@Transactional(readOnly = true)
@ReplicaDatasourceSemaphore
class StoreQueryRepositoryServiceImpl implements StoreQueryRepositoryService {

     private final StoreQueryRepository storeQueryRepository;
     private final SnsQueryRepository snsQueryRepository;
     private final TagQueryRepository tagQueryRepository;

     StoreQueryRepositoryServiceImpl(StoreQueryRepository storeQueryRepository,
               SnsQueryRepository snsQueryRepository, TagQueryRepository tagQueryRepository) {
          this.storeQueryRepository = storeQueryRepository;
          this.snsQueryRepository = snsQueryRepository;
          this.tagQueryRepository = tagQueryRepository;
     }

     @ResponseCacheable(type = "store", timeoutMinutes = 60)
     public StoreResponse findByUserId(UserId userId) {
		StoreResponse storeResponse = storeQueryRepository.findByUserId(userId);

          List<SnsResponse> sns = snsQueryRepository.findByUserId(userId);
          List<StoreTagResponse> tags = tagQueryRepository.findStoreTagsByUserId(userId);

          return storeResponse.withSnsAndTags(sns, tags);
	}

     @Override
     public List<SearchStoreResponse> findByKeyword(String keyword, UserId lastUserId, boolean isAdult, int amount) {
          List<SearchStoreResponse> responses = storeQueryRepository.findByKeyword(keyword, lastUserId, isAdult, amount);

          Set<UserId> userIds = responses.stream()
               .map(e -> e.userId())
               .collect(Collectors.toSet());

          Map<Long, List<StoreTagResponse>> tags = tagQueryRepository.findStoreTagsByUserIds(userIds).stream()
               .collect(Collectors.groupingBy(e -> e.userId().value()));

          return responses.stream()
               .map(e -> e.withTags(tags.get(e.userId().value())))
               .toList();
     }

     @Override
     public List<SearchStoreResponse> findRandom(boolean isAdult, int amount) {
          List<SearchStoreResponse> responses = storeQueryRepository.findRandom(isAdult, amount);

          Set<UserId> userIds = responses.stream()
               .map(e -> e.userId())
               .collect(Collectors.toSet());

          Map<Long, List<StoreTagResponse>> tags = tagQueryRepository.findStoreTagsByUserIds(userIds).stream()
               .collect(Collectors.groupingBy(e -> e.userId().value()));

          return responses.stream()
               .map(e -> e.withTags(tags.get(e.userId().value())))
               .toList();
     }

     @Override
     public StoreForCertificateCardResponse findStoreForCertificateCardByUserId(UserId userId) {
          return storeQueryRepository.findStoreForCertificateCardByUserId(userId);
     }

     @Override
     public List<String> findStoreNames(String keyword, boolean isAdult, int amount) {
          return storeQueryRepository.findStoreNames(keyword, isAdult, amount);
     }

     @Override
     public List<SearchBoardStoreResponse> findBoardStoresByName(String storeName) {
          return storeQueryRepository.findBoardStoresByName(storeName);
     }

     @Override
     public List<StoreGeoResponse> findStoreGeosByUserIds(List<UserId> userIds, List<String> tagLists, String current, boolean isAdult) {
          return storeQueryRepository.findStoreGeosByUserIds(userIds, tagLists, current, isAdult);
     }
}
