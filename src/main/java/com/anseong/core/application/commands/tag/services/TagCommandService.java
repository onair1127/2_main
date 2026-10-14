package com.anseong.core.application.commands.tag.services;

import com.anseong.core.application.commands.tag.AddCommonTagCommand;
import com.anseong.core.application.commands.tag.AddPersonalTagCommand;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.user.UserId;

public interface TagCommandService {
	void addCommonTag(UserId userId, String tagName);

     void addCommonTag(AddCommonTagCommand command, UserId userId);

	void addPersonalTag(AddPersonalTagCommand command, UserId userId);

     void deleteCommonTags(UserId userId, String tagName);

	void deletePersonalTag(ImageId imageId, String tagName);

     void deleteRecommendTag(String tagName);
}
