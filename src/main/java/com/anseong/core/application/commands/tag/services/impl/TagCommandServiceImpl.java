package com.anseong.core.application.commands.tag.services.impl;

import org.springframework.stereotype.Service;

import com.anseong.core.application.commands.tag.AddCommonTagCommand;
import com.anseong.core.application.commands.tag.AddPersonalTagCommand;
import com.anseong.core.application.commands.tag.services.TagCommandRepositoryService;
import com.anseong.core.application.commands.tag.services.TagCommandService;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.tag.repository.TagCache;
import com.anseong.core.domain.model.user.UserId;

import io.jsonwebtoken.lang.Assert;

@Service
class TagCommandServiceImpl implements TagCommandService {

     private final TagCommandRepositoryService tagCommandRepositoryService;
     private final TagCache tagCache;

     TagCommandServiceImpl(
          TagCommandRepositoryService tagCommandRepositoryService,
          TagCache tagCache
     ) {
          this.tagCommandRepositoryService = tagCommandRepositoryService;
          this.tagCache = tagCache;
     }

	@Override
	public void addCommonTag(UserId userId, String tagName) {
          tagCommandRepositoryService.saveCommonTag(userId, tagName);
	}

	@Override
     public void addCommonTag(AddCommonTagCommand command, UserId userId) {
          tagCommandRepositoryService.saveCommonTag(command, userId);
     }

     @Override
	public void addPersonalTag(AddPersonalTagCommand command, UserId userId) {
          tagCommandRepositoryService.savePersonalTag(command.imageId(), userId, command.tagName());
	}

     @Override
	public void deleteCommonTags(UserId userId, String tagName) {
          Assert.hasText(tagName, "태그 서비스 : 파라미터 `tag`가 비어있습니다.");
          Assert.notNull(userId, "태그 서비스 : 파라미터 `userId`가 비어있습니다.");

          tagCommandRepositoryService.removeCommonTag(userId, tagName);
	}

     @Override
	public void deletePersonalTag(ImageId imageId, String tagName) {
          tagCommandRepositoryService.removePersonalTag(imageId, tagName);
	}

     @Override
     public void deleteRecommendTag(String tag) {
          tagCache.deleteRecommendTag(tag);
     }
}
