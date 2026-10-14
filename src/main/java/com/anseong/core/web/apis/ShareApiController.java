package com.anseong.core.web.apis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.repository.ImageCache;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.web.apis.authenticate.CurrentUser;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.result.Result;

import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
class ShareApiController {

     private final ImageCache imageCache;

     ShareApiController(ImageCache imageCache) {
          this.imageCache = imageCache;
     }

     @PostMapping("/api/click/share/{storeId}/{imageId}")
     ResponseEntity<ApiResult> increaseClickShareCount(
          @NotNull @PathVariable("storeId") long storeId,
          @NotNull @PathVariable("imageId") long imageId,
          @CurrentUser SimpleUser user
     ) {
          imageCache.incrementClickShareCount(new UserId(storeId), new ImageId(imageId));
          return Result.created();
     }
}
