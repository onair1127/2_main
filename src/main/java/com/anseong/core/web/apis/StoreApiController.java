package com.anseong.core.web.apis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.anseong.core.application.queries.image.services.ImageQueryService;
import com.anseong.core.application.queries.store.ResearchStoreGeoResponse;
import com.anseong.core.application.queries.store.services.StoreQueryService;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.store.repository.StoreCache;
import com.anseong.core.domain.model.store.response.SearchBoardStoreResponse;
import com.anseong.core.domain.model.store.response.SearchStoreResponse;
import com.anseong.core.domain.model.store.response.StoreResponse;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.web.apis.authenticate.CurrentUser;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.result.CachedImageResults;
import com.anseong.core.web.result.Result;
import com.anseong.core.web.result.SearchResults;
import com.anseong.core.web.result.StoreGeoResults;
import com.anseong.core.web.result.StoreResult;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/store")
class StoreApiController {

     private final StoreQueryService storeQueryService;
     private final ImageQueryService imageQueryService;
     private final StoreCache storeCache;

     public StoreApiController(
          StoreQueryService storeQueryService
          , ImageQueryService imageQueryService
          , StoreCache storeCache
     ) {
          this.storeQueryService = storeQueryService;
          this.imageQueryService = imageQueryService;
          this.storeCache = storeCache;
     }

     @GetMapping("/{storeId}")
     ResponseEntity<ApiResult> getStore(
               @PathVariable("storeId") long storeId,
               @CurrentUser SimpleUser user
     ) {
          StoreResponse store = storeQueryService.getStore(new UserId(storeId));

          if (store == null) {
               return Result.notFound();
          }

          return StoreResult.build(store);
     }

     @GetMapping("/image/{storeId}")
     ResponseEntity<ApiResult> getImages(
               @PathVariable("storeId") Long storeId,
               @RequestParam("lastImageId") long lastImageId,
               @RequestParam("type") String type,
               @CurrentUser SimpleUser user
     ) {
          List<ImageResponse> images = imageQueryService.getStoreImages(new UserId(storeId), new ImageId(lastImageId), type, user.isAdult());
          return CachedImageResults.build(images, null, null, false, false);
     }

     @GetMapping("/search/{keyword}")
     ResponseEntity<ApiResult> searchStores(
               @PathVariable("keyword") String keyword,
               @RequestParam("lastStoreId") long lastStoreId,
               @CurrentUser SimpleUser user
     ) {
          List<SearchStoreResponse> stores = storeQueryService.search(keyword, user.getUserId(), new UserId(lastStoreId), user.isAdult());
          return SearchResults.build(stores);
     }

     @GetMapping("/randomSearchStores")
     ResponseEntity<ApiResult> getRandomSearchStores(
          @CurrentUser SimpleUser user
     ) {
          List<SearchStoreResponse> stores = storeQueryService.getRandomSearchStores(user.isAdult());
          return SearchResults.build(stores);
     }


     @GetMapping("/history/search")
     ResponseEntity<ApiResult> getSearchHistory(
               @CurrentUser SimpleUser user
     ) {
          List<String> result = storeCache.getSearchHistory(user.getUserId());
          return Result.ok(ApiResult.blank().add("history", result));
     }

     @DeleteMapping("/history/search/delete/{history}")
     ResponseEntity<ApiResult> deleteSearchHistory(
               @NotNull @NotEmpty @PathVariable("history") String history,
               @CurrentUser SimpleUser user
     ) {
          storeCache.deleteSearchHistory(user.getUserId(), history);
          return Result.noContent();
     }

     @GetMapping("/name/search/{keyword}")
     ResponseEntity<ApiResult> getStoreNames(
               @NotNull @NotEmpty @PathVariable("keyword") String keyword,
               @CurrentUser SimpleUser user
     ) {
          List<String> results = storeQueryService.getStoreNames(keyword, user.isAdult());
          return Result.ok(ApiResult.blank().add("results", results));
     }

     @GetMapping("/board/{storeName}")
     ResponseEntity<ApiResult> getBoardStores(
               @NotNull @PathVariable("storeName") String storeName,
               @CurrentUser SimpleUser user
     ) {
          List<SearchBoardStoreResponse> stores = storeQueryService.getBoardStores(storeName);
          Map<Object, Object> results = new HashMap<>();

          for (SearchBoardStoreResponse s : stores) {
               results.put(s.storeName(), s.userId().value());
          }

          return Result.ok(ApiResult.blank().add("stores", results));
     }

     @PostMapping("/click/sns/{storeId}/{imageId}")
     ResponseEntity<ApiResult> increaseClickShareCount(
          @NotNull @PathVariable("storeId") long storeId,
          @NotNull @PathVariable("imageId") String type,
          @CurrentUser SimpleUser user
     ) {
          storeCache.incrementClickSnsCount(new UserId(storeId), type);
          return Result.created();
     }

     @GetMapping("/get/store/geo")
     ResponseEntity<ApiResult> researchStoresFromLocation(
          @NotNull @RequestParam(name = "lat", defaultValue = "0.0") double lat,
          @NotNull @RequestParam(name = "lng", defaultValue = "0.0") double lng,
          @CurrentUser SimpleUser user
     ) {
          ResearchStoreGeoResponse response = storeQueryService.researchStoresFromCoords(lat, lng, user.isAdult());
          return StoreGeoResults.build(response.storeGeoResponses(), response.address());
     }
}
