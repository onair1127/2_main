package com.anseong.core.application.commands.image.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.commands.image.UpdateImageCommand;
import com.anseong.core.application.commands.image.UploadImageCommand;
import com.anseong.core.application.commands.image.services.ImageCommandRepositoryService;
import com.anseong.core.domain.aop.semaphore.MasterDatasourceSemaphore;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;
import com.anseong.core.domain.model.image.Image;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.repository.ImageCommandRepository;
import com.anseong.core.domain.model.store.ImageStore;
import com.anseong.core.domain.model.store.repository.StoreCommandRepository;
import com.anseong.core.domain.model.user.ImageUser;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.repository.UserCommandRepository;
import com.anseong.core.domain.model.user.repository.UserQueryRepository;

@Service
@Transactional
@MasterDatasourceSemaphore
class ImageCommandRepositoryServiceImpl implements ImageCommandRepositoryService {

     private final ImageCommandRepository imageCommandRepository;
     private final UserQueryRepository userQueryRepository;
     private final UserCommandRepository userCommandRepository;
     private final StoreCommandRepository storeCommandRepository;

     ImageCommandRepositoryServiceImpl(
          ImageCommandRepository imageCommandRepository
          , UserQueryRepository userQueryRepository
          , UserCommandRepository userCommandRepository
          , StoreCommandRepository storeCommandRepository
     ) {
          this.imageCommandRepository = imageCommandRepository;
          this.userQueryRepository = userQueryRepository;
          this.userCommandRepository = userCommandRepository;
          this.storeCommandRepository = storeCommandRepository;
     }

     @Override
     public void save(UploadImageCommand command, List<ConvertMediaResponse> convertMediaResponse, UserId storeId, String nickname) {
          if (command.type().equals("review"))
               storeId = userQueryRepository.findIdByPhone(command.phone());

          ImageUser imageUser = userCommandRepository.getImageUserReferenceById(storeId);
          ImageStore imageStore = storeCommandRepository.getImageStoreReferenceById(storeId);
          Image image = Image.create(command, convertMediaResponse, imageUser, imageStore);

          imageCommandRepository.save(image);
     }

     @Override
	public void update(UpdateImageCommand command, UserId userId, String type) {
          Image updatableImage = imageCommandRepository.findById(command.imageId());

          updatableImage.update(command);
	}

	@Override
     public void remove(ImageId imageId) {
          imageCommandRepository.removeById(imageId);
	}
}
