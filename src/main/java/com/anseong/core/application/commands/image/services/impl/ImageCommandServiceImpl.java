package com.anseong.core.application.commands.image.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.anseong.core.application.commands.image.DeleteImageCommand;
import com.anseong.core.application.commands.image.UpdateImageCommand;
import com.anseong.core.application.commands.image.UploadImageCommand;
import com.anseong.core.application.commands.image.services.ImageCommandRepositoryService;
import com.anseong.core.application.commands.image.services.ImageCommandService;
import com.anseong.core.domain.aop.cache.ResponseCacheEvict;
import com.anseong.core.domain.client.convert.media.ConvertMediaClient;
import com.anseong.core.domain.client.convert.media.request.ConvertMediaRequest;
import com.anseong.core.domain.client.convert.media.request.ConvertMediaRequest.ConvertMode;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;
import com.anseong.core.domain.model.image.repository.ImageCache;
import com.anseong.core.domain.model.user.UserId;

@Service
class ImageCommandServiceImpl implements ImageCommandService {

     private final ConvertMediaClient convertMediaClient;
     private final ImageCommandRepositoryService imageCommandRepositoryService;
     private final ImageCache imageCache;

     ImageCommandServiceImpl(
          ConvertMediaClient convertMediaClient
          , ImageCommandRepositoryService imageCommandRepositoryService
          , ImageCache imageCache
     ) {
          this.convertMediaClient = convertMediaClient;
          this.imageCommandRepositoryService = imageCommandRepositoryService;
          this.imageCache = imageCache;
     }

     @Override
     @ResponseCacheEvict
     public void uploadImage(UploadImageCommand command, UserId storeId, String nickname, String type) {
          Assert.notNull(command, "파라미터 command는 반드시 존재하여야 합니다.");

          List<ConvertMediaRequest> convertMediaRequests = command.images().stream()
               .map(e -> ConvertMediaRequest.builder()
                    .url(e.url())
                    .ext(e.ext())
                    .filename(e.filename())
                    .username(nickname)
                    .sequence(e.sequence())
                    .isSquare(e.isSquare())
                    .convertMode(ConvertMode.IMAGE)
                    .build()
               )
               .toList();

          List<ConvertMediaResponse> convertMediaResponses = convertMediaClient.process(convertMediaRequests);

          imageCommandRepositoryService.save(command, convertMediaResponses, storeId, nickname);
     }

	@Override
     @ResponseCacheEvict
     public void deleteImage(DeleteImageCommand command) {
          Assert.notNull(command.imageId(), "이미지 서비스 : imageId가 비어있습니다.");

          imageCommandRepositoryService.remove(command.imageId());

          if (!command.isArchived()) {
               imageCache.deleteFavoriteAll(command.imageId());
          }
	}

	@Override
     @ResponseCacheEvict
	public void updateImage(UpdateImageCommand command, UserId userId, String type) {
          imageCommandRepositoryService.update(command, userId, type);
	}
}
