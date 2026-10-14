package com.anseong.core.application.commands.store.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.anseong.core.application.commands.store.MakeOwnStoreCommand;
import com.anseong.core.application.commands.store.UpdateMyStoreCommand;
import com.anseong.core.application.commands.store.services.StoreCommandRepositoryService;
import com.anseong.core.application.commands.store.services.StoreCommandService;
import com.anseong.core.domain.aop.cache.ResponseCacheEvict;
import com.anseong.core.domain.client.convert.media.ConvertMediaClient;
import com.anseong.core.domain.client.convert.media.request.ConvertMediaRequest;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;
import com.anseong.core.domain.client.messanger.MessangerClient;
import com.anseong.core.domain.client.messanger.MessangerTemplate;
import com.anseong.core.domain.client.messanger.MessangerType;
import com.anseong.core.domain.client.messanger.request.MessangerRequest;
import com.anseong.core.domain.model.store.exception.StoreDuplicatedException;
import com.anseong.core.domain.model.store.repository.StoreCache;
import com.anseong.core.domain.model.user.UserId;

@Service
class StoreCommandServiceImpl implements StoreCommandService {

     private final StoreCommandRepositoryService storeCommandRepositoryService;
     private final MessangerClient messangerClient;
     private final ConvertMediaClient convertMediaClient;
     private final StoreCache storeCache;

     StoreCommandServiceImpl(
          StoreCommandRepositoryService storeCommandRepositoryService,
          MessangerClient messangerClient,
          ConvertMediaClient convertMediaClient,
          StoreCache storeCache
     ) {
          this.storeCommandRepositoryService = storeCommandRepositoryService;
          this.messangerClient = messangerClient;
          this.convertMediaClient = convertMediaClient;
          this.storeCache = storeCache;
     }

     @Override
     public void validateStore(UserId userId, long bizNo) throws StoreDuplicatedException {
          Long duplicated = storeCommandRepositoryService.findDuplicatedStore(userId, bizNo);

          if (duplicated > 0) {
               throw new StoreDuplicatedException("이미 등록된 가게가 존재합니다.");
          }
     }

	@Override
	public void createStore(MakeOwnStoreCommand command, UserId userId, String nickname) {
          Assert.notNull(command, "가게 서비스 : 파라미터 `command` 값이 null입니다.");
          Assert.notNull(userId, "가게 서비스 : 파라미터 `userId` 값이 null입니다.");

          ConvertMediaRequest thumbnailRequest = CreateThumbnailManagement.createThumbnailRequest(command.thumbnail(), nickname);
          ConvertMediaResponse convertedThumbnail = convertMediaClient.process(thumbnailRequest);

          storeCommandRepositoryService.save(userId, command, convertedThumbnail);
          storeCache.createStore(userId, command.name(), command.addSns(), command.lat(), command.lng());

          if (command.addSns().size() > 0) {
               MessangerRequest request = MessangerRequest.builder()
                    .type(MessangerType.SMS)
                    .template(MessangerTemplate.ADMIN_SNS_JOB)
                    .username(command.phone())
                    .build();

               messangerClient.sendReportSMSNotificationToMe(request);
          }
	}

	@Override
     @ResponseCacheEvict(type = "store")
	public void updateStore(UpdateMyStoreCommand command, String nickname, UserId userId) {
          Assert.notNull(command, "가게 서비스 : 파라미터 `command`가 비어있습니다.");

          ConvertMediaResponse convertedThumbnail = null;

          if (command.thumbnailCreated()) {
               ConvertMediaRequest thumbnailRequest = CreateThumbnailManagement.createThumbnailRequest(command.thumbnail(), nickname);
               convertedThumbnail = convertMediaClient.process(thumbnailRequest);
          }

          storeCommandRepositoryService.update(userId, command, convertedThumbnail);
          storeCache.updateGeoLocation(userId, command.lat(), command.lng());
	}
}
