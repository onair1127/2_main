package com.anseong.core.web.apis;

import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.anseong.core.application.commands.report.services.ReportCommandService;
import com.anseong.core.application.queries.image.GetImageQuery;
import com.anseong.core.application.queries.image.GetImageResponse;
import com.anseong.core.application.queries.image.MainImageQuery;
import com.anseong.core.application.queries.image.MainImageResponse;
import com.anseong.core.application.queries.image.services.ImageQueryService;
import com.anseong.core.application.queries.tag.services.TagQueryService;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.repository.ImageCache;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.web.apis.authenticate.CurrentUser;
import com.anseong.core.web.payloads.ReportImagePayload;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.result.CachedImageResults;
import com.anseong.core.web.result.ImageResult;
import com.anseong.core.web.result.ImageResults;
import com.anseong.core.web.result.RandomTagsAndImagesResults;
import com.anseong.core.web.result.Result;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class ImageApiController {

     private final ImageQueryService imageQueryService;
     private final ImageCache imageCache;
     private final TagQueryService tagQueryService;
     private final ReportCommandService reportCommandService;

     public ImageApiController(
          ImageQueryService imageQueryService
          , ImageCache imageCache
          , TagQueryService tagQueryService
          , ReportCommandService reportCommandService
     ) {
          this.imageQueryService = imageQueryService;
          this.imageCache = imageCache;
          this.tagQueryService = tagQueryService;
          this.reportCommandService = reportCommandService;
     }

     @GetMapping("/image/one")
     ResponseEntity<ApiResult> getImageOne(
               @CurrentUser SimpleUser user
     ) {
          ImageResponse image = imageQueryService.getRandomOne(user.isAdult());
          return ImageResult.build(image);
     }

     @GetMapping("/images")
     ResponseEntity<ApiResult> getImages(
               @NotNull @RequestParam("tag") List<String> tagLists,
               @NotNull @RequestParam("id") List<Long> imageIds,
               @NotNull @RequestParam("sid") List<Long> storeIds,
               @Nullable @RequestParam("current") String current,
               @Nullable @RequestParam(name = "job", defaultValue = "false") boolean job,
               @Nullable @RequestParam(name = "lat", defaultValue = "0.0") double lat,
               @Nullable @RequestParam(name = "lng", defaultValue = "0.0") double lng,
               @CurrentUser SimpleUser user
     ) throws Exception {
          GetImageQuery query = GetImageQuery.of(tagLists, imageIds, storeIds, current, job, lat, lng);
          GetImageResponse response = imageQueryService.getImages(query, user.isAdult());
          return ImageResults.build(response.images(), response.address(), response.storeGeoResponses(), true, false);
     }

     @GetMapping("/images/random")
     ResponseEntity<ApiResult> getRandomTagsAndImages(
          @NotNull @RequestParam("current") String current,
          @Nullable @RequestParam("lat") Double lat,
          @Nullable @RequestParam("lng") Double lng,
          @CurrentUser SimpleUser user
     ) {
          MainImageQuery query = new MainImageQuery(current, user.isAdult(), lat, lng);
          MainImageResponse result = imageQueryService.getRandomTagsAndImages(query);
          return RandomTagsAndImagesResults.build(result.randomRecommendTags(), result.randomTagImages(), result.storeGeos(), result.address());
     }

     @GetMapping("/images/tag/search/{keyword}")
     ResponseEntity<ApiResult> getStoreNames(
               @NotNull @NotEmpty @PathVariable("keyword") String keyword,
               @CurrentUser SimpleUser user
     ) {
          return Result.ok(ApiResult.blank().add("result", tagQueryService.getTagNames(keyword)));
     }

     @GetMapping("/favorites")
     ResponseEntity<ApiResult> getFavorites(
               @Nullable @RequestParam("id") List<Long> imageIds,
               @CurrentUser SimpleUser user
     ) {
          List<ImageResponse> images = imageQueryService.getFavorites(user.getUserId(), imageIds.stream().map(ImageId::new).toList());
          return ImageResults.build(images, null, null, true, false);
     }

     @PostMapping("/favorite/{imageId}")
     ResponseEntity<ApiResult> addFavorite(
               @PathVariable("imageId") long imageId,
               @CurrentUser SimpleUser user
     ) {
          imageCache.addFavorite(user.getUserId(), new ImageId(imageId));
          return Result.created();
     }

     @DeleteMapping("/favorite/{imageId}")
     ResponseEntity<ApiResult> deleteFavorite(
               @PathVariable("imageId") long imageId,
               @CurrentUser SimpleUser user
     ) {
          imageCache.deleteFavorite(user.getUserId(), new ImageId(imageId));
          return Result.ok();
     }

     @GetMapping("/images/{type}/{page}")
     ResponseEntity<ApiResult> getStoreInfo(
               @PathVariable("type") String type,
               @PathVariable("page") long lastImageId,
               @CurrentUser SimpleUser user
     ) {
          List<ImageResponse> images = imageQueryService.getImagesByType(type, new ImageId(lastImageId), user.isAdult());
          return CachedImageResults.build(images, null, null, true, false);
     }

     @PostMapping("/image/report")
     ResponseEntity<ApiResult> reportImage(
               @RequestBody ReportImagePayload payload,
               @CurrentUser SimpleUser user
     ) {
          reportCommandService.reportImage(user.getUserId(), payload.toCommand());
          return Result.ok();
     }
}
