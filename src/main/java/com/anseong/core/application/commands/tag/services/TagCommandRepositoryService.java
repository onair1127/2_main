package com.anseong.core.application.commands.tag.services;

import com.anseong.core.application.commands.tag.AddCommonTagCommand;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.user.UserId;

public interface TagCommandRepositoryService {
     void saveCommonTag(UserId userId, String tagName);

     void saveCommonTag(AddCommonTagCommand command, UserId userId);

     void savePersonalTag(ImageId imageId, UserId userId, String tagName);

     void removeCommonTag(UserId userId, String tagName);

     void removePersonalTag(ImageId imageId, String tagName);
}
