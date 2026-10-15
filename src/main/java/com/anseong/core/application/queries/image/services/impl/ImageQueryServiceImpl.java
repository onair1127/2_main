package com.anseong.core.application.queries.image.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;

import com.anseong.core.application.queries.image.GetImageQuery;
import com.anseong.core.application.queries.image.GetImageResponse;
import com.anseong.core.application.queries.image.MainImageQuery;
import com.anseong.core.application.queries.image.MainImageResponse;
import com.anseong.core.application.queries.image.services.ImageQueryRepositoryService;
import com.anseong.core.application.queries.image.services.ImageQueryService;
import com.anseong.core.application.queries.store.services.StoreQueryRepositoryService;
import com.anseong.core.domain.client.location.LocationClient;
import com.anseong.core.domain.client.location.request.AddressRequest;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.repository.ImageCache;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.store.repository.StoreCache;
import com.anseong.core.domain.model.store.repository.StoreQueryRepository;
import com.anseong.core.domain.model.store.response.StoreGeoResponse;
import com.anseong.core.domain.model.tag.repository.TagCache;
import com.anseong.core.domain.model.user.UserId;

@Service
class ImageQueryServiceImpl implements ImageQueryService {

     private final ImageQueryRepositoryService imageQueryRepositoryService;
     private final StoreQueryRepositoryService storeQueryRepositoryService;
     private final LocationClient locationClient;
     private final ImageCache imageCache;
     private final StoreCache storeCache;
     private final TagCache tagCache;

     private final AsyncTaskExecutor virtualTaskExecutor;

     ImageQueryServiceImpl(
               ImageQueryRepositoryService imageQueryRepositoryService,
               StoreQueryRepositoryService storeQueryRepositoryService,
               LocationClient locationClient,
               ImageCache imageCache,
               StoreCache storeCache,
               TagCache tagCache,
               AsyncTaskExecutor virtualTaskExecutor
     ) {
          this.imageQueryRepositoryService = imageQueryRepositoryService;
          this.storeQueryRepositoryService = storeQueryRepositoryService;
          this.locationClient = locationClient;
          this.imageCache = imageCache;
          this.storeCache = storeCache;
          this.tagCache = tagCache;
          this.virtualTaskExecutor = virtualTaskExecutor;
     }

     @Override
     public ImageResponse getRandomOne(boolean isAdult) {
          ImageResponse image = imageQueryRepositoryService.findRandomOne(isAdult);
          image = imageCache.setImageFavoritesAndExposedCountOne(image);

          return image;
     }

     @Override
     public MainImageResponse getRandomTagsAndImages(MainImageQuery query) {
          CompletableFuture<MainImageResponse> mainImageTask = CompletableFuture.supplyAsync(
               () -> {
                    Map<String, String> recommendTags = tagCache.getRandomRecommendTags();
                    Map<String, List<ImageResponse>> randomTagImages = imageQueryRepositoryService.findGroupByTagName(new ArrayList<>(recommendTags.keySet()), query.current(), query.isAdult(), 11);
                    List<UserId> storeIdsNearUser = storeCache.getGeoStoreIds(query.lat(), query.lng());
                    List<StoreGeoResponse> storeGeos = storeQueryRepositoryService.findStoreGeosByUserIds(storeIdsNearUser, null, null, query.isAdult());

                    List<ImageResponse> images = randomTagImages.values().stream()
                         .flatMap(Collection::stream)
                         .toList();

                    images = imageCache.setImageFavoritesAndExposedCountFromResponse(images);

                    randomTagImages = images.stream()
                         .collect(Collectors.groupingBy(e -> e.referenceTagName()));

                    return MainImageResponse.builder()
                         .randomRecommendTags(recommendTags)
                         .randomTagImages(randomTagImages)
                         .storeGeos(storeGeos)
                         .build();
               }
          , virtualTaskExecutor);

          CompletableFuture<String> addressTask = CompletableFuture.supplyAsync(
               () -> {
                    if (query.lat() == null || query.lng() == null)
                         return null;
                    else
                         try {
                              AddressRequest request = AddressRequest.builder()
                                   .lat(query.lat())
                                   .lng(query.lng())
                                   .build();

                              return locationClient.getAddressByCoords(request);
                         } catch (Exception e) {
                              e.printStackTrace();
                              return null;
                         }
               }
          , virtualTaskExecutor);

          CompletableFuture<MainImageResponse> response = mainImageTask
               .thenCombine(addressTask, (main, address) -> main.withAddress(address));

          return response.join();
     }


     @Override
     public GetImageResponse getImages(GetImageQuery query, boolean isAdult) {
          CompletableFuture<GetImageResponse> getImageTask = CompletableFuture.supplyAsync(
               () -> {
                    List<ImageResponse> imageResponses = imageQueryRepositoryService.findRandom(query, isAdult, 12);
                    List<UserId> storesIdsNearUser = storeCache.getGeoStoreIds(query.lat(), query.lng());
                    List<StoreGeoResponse> storeGeoResponses = storeQueryRepositoryService.findStoreGeosByUserIds(storesIdsNearUser, query.tagLists(), query.current(), isAdult);

                    imageResponses = imageCache.setImageFavoritesAndExposedCountFromResponse(imageResponses);

                    tagCache.determineTag(query.tagLists());

                    return GetImageResponse.builder()
                         .images(imageResponses)
                         .storeGeoResponses(storeGeoResponses)
                         .build();
               }
          , virtualTaskExecutor);

          CompletableFuture<String> addressTask = CompletableFuture.supplyAsync(
               () -> {
                    if (query.lat() == 0 || query.lng() == 0)
                         return null;
                    else
                         try {
                              AddressRequest request = AddressRequest.builder()
                                   .lat(query.lat())
                                   .lng(query.lng())
                                   .build();

                              return locationClient.getAddressByCoords(request);
                         } catch (Exception e) {
                              return null;
                         }
               }
          , virtualTaskExecutor);

          CompletableFuture<GetImageResponse> response = getImageTask
               .thenCombine(addressTask, (getImage, address) -> getImage.withAddress(address));

          return response.join();
     }

     @Override
     public List<ImageResponse> getImagesByType(String type, ImageId lastImageId, boolean isAdult) {
          List<ImageResponse> cachedImages = imageQueryRepositoryService.findByType(lastImageId, isAdult, type, 10);
          cachedImages = imageCache.setImageFavoritesAndExposedCountFromResponse(cachedImages);

		return cachedImages;
	}

     @Override
	public List<ImageResponse> getStoreImages(UserId storeId, ImageId lastImageId, String type, boolean isAdult) {
          List<ImageResponse> images = imageQueryRepositoryService.findStoreImages(storeId, lastImageId, type, isAdult, 12);
          images = imageCache.setImageFavoritesAndExposedCountFromResponse(images);

		return images;
	}

     @Override
     public List<ImageResponse> getFavorites(UserId userId, List<ImageId> imageIds) {
          List<ImageResponse> images = imageQueryRepositoryService.findByIds(imageIds, false);
          images = imageCache.setImageFavoritesAndExposedCountFromResponse(images);

		return images;
	}

     @Override
     public List<ImageResponse> getUpdatableImages(UserId userId, ImageId lastImageId, boolean isAdult) {
          return imageQueryRepositoryService.findByUserId(userId, lastImageId, isAdult, 12);
     }
}
