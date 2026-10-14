package com.anseong.core.application.commands.tag.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.commands.tag.AddCommonTagCommand;
import com.anseong.core.application.commands.tag.services.TagCommandRepositoryService;
import com.anseong.core.domain.aop.semaphore.MasterDatasourceSemaphore;
import com.anseong.core.domain.model.image.Image;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.repository.ImageCommandRepository;
import com.anseong.core.domain.model.image.repository.ImageQueryRepository;
import com.anseong.core.domain.model.tag.Tag;
import com.anseong.core.domain.model.tag.repository.TagCommandRepository;
import com.anseong.core.domain.model.user.UserId;

@Service
@Transactional
@MasterDatasourceSemaphore
class TagCommandRepositoryServiceImpl implements TagCommandRepositoryService {

     private final TagCommandRepository tagCommandRepository;
     private final ImageCommandRepository imageCommandRepository;
     private final ImageQueryRepository imageQueryRepository;

     TagCommandRepositoryServiceImpl(
          TagCommandRepository tagCommandRepository,
          ImageCommandRepository imageCommandRepository,
          ImageQueryRepository imageQueryRepository
     ) {
          this.tagCommandRepository = tagCommandRepository;
          this.imageCommandRepository = imageCommandRepository;
          this.imageQueryRepository = imageQueryRepository;
     }

     @Override
     public void saveCommonTag(UserId userId, String tagName) {
          List<Image> imageReferences = imageQueryRepository.findIdsByUserId(userId).stream()
               .map(e -> imageCommandRepository.getReferenceById(e))
               .toList();

          List<Tag> tags = Tag.craete(imageReferences, userId, tagName, "common");
          tagCommandRepository.save(tags);
     }

     @Override
     public void saveCommonTag(AddCommonTagCommand command, UserId userId) {
          List<Image> imageReferences = command.imageIds().stream()
               .map(e -> imageCommandRepository.getReferenceById(e))
               .toList();

          List<Tag> tags = Tag.craete(imageReferences, userId, command.tagName(), "common");
          tagCommandRepository.save(tags);
     }

     @Override
     public void savePersonalTag(ImageId imageId, UserId userId, String tagName) {
          Image imageReference = imageCommandRepository.getReferenceById(imageId);
          Tag tag = Tag.craete(imageReference, userId, tagName, "personal");

          tagCommandRepository.save(tag);
     }

     @Override
     public void removeCommonTag(UserId userId, String tagName) {
          tagCommandRepository.deleteByUserIdAndTagName(userId, tagName);
     }

     @Override
     public void removePersonalTag(ImageId imageId, String tagName) {
          tagCommandRepository.deleteByImageIdAndTagName(imageId, tagName);
     }
}
