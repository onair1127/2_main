package com.anseong.core.application.queries.image.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.queries.image.GetImageQuery;
import com.anseong.core.application.queries.image.services.ImageQueryRepositoryService;
import com.anseong.core.domain.aop.cache.ResponseCacheable;
import com.anseong.core.domain.aop.semaphore.ReplicaDatasourceSemaphore;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.repository.ImageQueryRepository;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.multiple_image.repository.MultipleImageQueryRepository;
import com.anseong.core.domain.model.multiple_image.response.MultipleImageResponse;
import com.anseong.core.domain.model.sns.repository.SnsQueryRepository;
import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.tag.repository.TagQueryRepository;
import com.anseong.core.domain.model.tag.response.TagResponse;
import com.anseong.core.domain.model.user.UserId;

@Service
@Transactional(readOnly = true)
@ReplicaDatasourceSemaphore
class ImageQueryRepositoryServiceImpl implements ImageQueryRepositoryService {

     private final ImageQueryRepository imageQueryRepository;
     private final SnsQueryRepository snsQueryRepository;
     private final TagQueryRepository tagQueryRepository;
     private final MultipleImageQueryRepository multipleImageQueryRepository;

     ImageQueryRepositoryServiceImpl(
               ImageQueryRepository imageQueryRepository
               , SnsQueryRepository snsQueryRepository
               , TagQueryRepository tagQueryRepository
               , MultipleImageQueryRepository multipleImageQueryRepository
     ) {
          this.imageQueryRepository = imageQueryRepository;
          this.snsQueryRepository = snsQueryRepository;
          this.tagQueryRepository = tagQueryRepository;
          this.multipleImageQueryRepository = multipleImageQueryRepository;
     }

     @Override
     public ImageResponse findRandomOne(boolean isAdult) {
          ImageResponse imageResponse = imageQueryRepository.findRandomOne(isAdult);

          List<SnsResponse> sns = snsQueryRepository.findByUserId(imageResponse.storeId());
          List<TagResponse> tags = tagQueryRepository.findByImageId(imageResponse.id());
          List<MultipleImageResponse> multipleImages = multipleImageQueryRepository.findByImageId(imageResponse.id());

          return imageResponse.withCollections(sns, tags, multipleImages);
     }

     @Override
     public Map<String, List<ImageResponse>> findGroupByTagName(List<String> tagNames, String current, boolean isAdult, int amountPerTag) {
          /**
           * 조건에 맞는 응답 데이터 중 ID 값만 추출한 뒤 참조한 tagName으로 grouping.
           */
          Map<String, List<ImageId>> idsGroupByTagName = imageQueryRepository.findIdsGroupByTagName(tagNames, current, isAdult, amountPerTag);

          /**
           * ID값으로 image 데이터 로드
           */
          List<ImageResponse> imageResponses = new ArrayList<>();
          for (String tagName : tagNames) {
               List<ImageId> imageIds = idsGroupByTagName.getOrDefault(tagName, List.of());
               imageResponses.addAll(imageQueryRepository.findByIds(imageIds, tagName, true));
          }

          /**
           * 1:N 테이블 데이터 조회를 위한 Key 수집
           */
          Set<ImageId> imageIds = imageResponses.stream()
               .map(e -> e.id())
               .collect(Collectors.toSet());

          Set<UserId> storeIds = imageResponses.stream()
               .map(e -> e.storeId())
               .collect(Collectors.toSet());

          /**
           * 수집한 Key들로 1:N 테이블 조회
           */
          Map<Long, List<SnsResponse>> snsResponses = snsQueryRepository.findByUserIds(storeIds).stream()
               .collect(Collectors.groupingBy(e -> e.userId().value()));

          Map<Long, List<TagResponse>> tagResponses = tagQueryRepository.findByImageIds(imageIds).stream()
               .collect(Collectors.groupingBy(e -> e.imageId().value()));

          Map<Long, List<MultipleImageResponse>> multipleImageResponses = multipleImageQueryRepository.findByImageIds(imageIds).stream()
               .collect(Collectors.groupingBy(e -> e.imageId().value()));

          /**
           * 1:N 데이터들을 image 데이터에 바인딩한 뒤 참조한 tagName(referenceTagName)으로 grouping 후 리턴
           */
          return imageResponses.stream()
               .map(e -> {
                    List<SnsResponse> sns = snsResponses.getOrDefault(e.storeId().value(), Collections.emptyList());
                    List<TagResponse> tags = tagResponses.getOrDefault(e.id().value(), Collections.emptyList());
                    List<MultipleImageResponse> multipleImages = multipleImageResponses.getOrDefault(e.id().value(), Collections.emptyList());

                    return e.withCollections(sns, tags, multipleImages);
               })
               .collect(Collectors.groupingBy(ImageResponse::referenceTagName));
     }

     @Override
     public List<ImageResponse> findRandom(GetImageQuery query, boolean isAdult, int amount) {
          List<ImageResponse> imageResponses = imageQueryRepository.findRandom(query.tagLists(), query.imageIds(), query.current(), query.job(), query.storeIds(), isAdult, amount);

          Set<ImageId> responseImageIds = imageResponses.stream()
               .map(e -> e.id())
               .collect(Collectors.toSet());
          Set<UserId> responseStoreIds = imageResponses.stream()
               .map(e -> e.storeId())
               .collect(Collectors.toSet());

          Map<Long, List<SnsResponse>> snsResponses = snsQueryRepository.findByUserIds(responseStoreIds).stream()
               .collect(Collectors.groupingBy(e -> e.userId().value()));

          Map<Long, List<TagResponse>> tagResponses = tagQueryRepository.findByImageIds(responseImageIds).stream()
               .collect(Collectors.groupingBy(e -> e.imageId().value()));

          Map<Long, List<MultipleImageResponse>> multipleImageResponses = multipleImageQueryRepository.findByImageIds(responseImageIds).stream()
               .collect(Collectors.groupingBy(e -> e.imageId().value()));

          return imageResponses.stream()
               .map(e -> {
                    List<SnsResponse> sns = snsResponses.getOrDefault(e.storeId().value(), Collections.emptyList());
                    List<TagResponse> tags = tagResponses.getOrDefault(e.id().value(), Collections.emptyList());
                    List<MultipleImageResponse> multipleImages = multipleImageResponses.getOrDefault(e.id().value(), Collections.emptyList());

                    return e.withCollections(sns, tags, multipleImages);
               })
               .toList();
     }

     @Override
     public List<ImageResponse> findByIds(List<ImageId> imageIds, boolean shuffle) {
          List<ImageResponse> imageResponses = imageQueryRepository.findByIds(imageIds, shuffle);

          Set<ImageId> responseImageIds = imageResponses.stream()
               .map(e -> e.id())
               .collect(Collectors.toSet());
          Set<UserId> responseStoreIds = imageResponses.stream()
               .map(e -> e.storeId())
               .collect(Collectors.toSet());

          Map<Long, List<SnsResponse>> snsResponses = snsQueryRepository.findByUserIds(responseStoreIds).stream()
               .collect(Collectors.groupingBy(e -> e.userId().value()));

          Map<Long, List<TagResponse>> tagResponses = tagQueryRepository.findByImageIds(responseImageIds).stream()
               .collect(Collectors.groupingBy(e -> e.imageId().value()));

          Map<Long, List<MultipleImageResponse>> multipleImageResponses = multipleImageQueryRepository.findByImageIds(responseImageIds).stream()
               .collect(Collectors.groupingBy(e -> e.imageId().value()));

          return imageResponses.stream()
               .map(e -> {
                    List<SnsResponse> sns = snsResponses.getOrDefault(e.storeId().value(), Collections.emptyList());
                    List<TagResponse> tags = tagResponses.getOrDefault(e.id().value(), Collections.emptyList());
                    List<MultipleImageResponse> multipleImages = multipleImageResponses.getOrDefault(e.id().value(), Collections.emptyList());

                    return e.withCollections(sns, tags, multipleImages);
               })
               .toList();
     }

     @Override
     @ResponseCacheable(timeoutMinutes = 60)
     public List<ImageResponse> findByType(ImageId lastImageId, boolean adult, String type, int amount) {
          List<ImageResponse> imageResponses = imageQueryRepository.findByType(lastImageId, adult, type, amount);

          Set<ImageId> responseImageIds = imageResponses.stream()
               .map(e -> e.id())
               .collect(Collectors.toSet());
          Set<UserId> responseStoreIds = imageResponses.stream()
               .map(e -> e.storeId())
               .collect(Collectors.toSet());

          Map<Long, List<SnsResponse>> snsResponses = snsQueryRepository.findByUserIds(responseStoreIds).stream()
               .collect(Collectors.groupingBy(e -> e.userId().value()));

          Map<Long, List<TagResponse>> tagResponses = tagQueryRepository.findByImageIds(responseImageIds).stream()
               .collect(Collectors.groupingBy(e -> e.imageId().value()));

          Map<Long, List<MultipleImageResponse>> multipleImageResponses = multipleImageQueryRepository.findByImageIds(responseImageIds).stream()
               .collect(Collectors.groupingBy(e -> e.imageId().value()));

          return imageResponses.stream()
               .map(e -> {
                    List<SnsResponse> sns = snsResponses.getOrDefault(e.storeId().value(), Collections.emptyList());
                    List<TagResponse> tags = tagResponses.getOrDefault(e.id().value(), Collections.emptyList());
                    List<MultipleImageResponse> multipleImages = multipleImageResponses.getOrDefault(e.id().value(), Collections.emptyList());

                    return e.withCollections(sns, tags, multipleImages);
               })
               .toList();
	}

     @Override
     public List<ImageResponse> findByUserId(UserId userId, ImageId lastImageId, boolean isAdult, int amount) {
          List<ImageResponse> imageResponses = imageQueryRepository.findByUserId(userId, lastImageId, isAdult, amount);

          Set<ImageId> responseImageIds = imageResponses.stream()
               .map(e -> e.id())
               .collect(Collectors.toSet());
          Set<UserId> responseStoreIds = imageResponses.stream()
               .map(e -> e.storeId())
               .collect(Collectors.toSet());

          Map<Long, List<SnsResponse>> snsResponses = snsQueryRepository.findByUserIds(responseStoreIds).stream()
               .collect(Collectors.groupingBy(e -> e.userId().value()));

          Map<Long, List<TagResponse>> tagResponses = tagQueryRepository.findByImageIds(responseImageIds).stream()
               .collect(Collectors.groupingBy(e -> e.imageId().value()));

          Map<Long, List<MultipleImageResponse>> multipleImageResponses = multipleImageQueryRepository.findByImageIds(responseImageIds).stream()
               .collect(Collectors.groupingBy(e -> e.imageId().value()));

          return imageResponses.stream()
               .map(e -> {
                    List<SnsResponse> sns = snsResponses.getOrDefault(e.storeId().value(), Collections.emptyList());
                    List<TagResponse> tags = tagResponses.getOrDefault(e.id().value(), Collections.emptyList());
                    List<MultipleImageResponse> multipleImages = multipleImageResponses.getOrDefault(e.id().value(), Collections.emptyList());

                    return e.withCollections(sns, tags, multipleImages);
               })
               .toList();
     }


     @Override
     @ResponseCacheable(type = "storeImage", timeoutMinutes = 60)
	public List<ImageResponse> findStoreImages(UserId storeId, ImageId lastImageId, String type, boolean adult, int amount) {
          return imageQueryRepository.findStoreImages(storeId, lastImageId, type, adult, amount);
	}
}
