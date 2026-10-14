package com.anseong.core.application.commands.image.services;

import java.util.List;

import com.anseong.core.application.commands.image.UpdateImageCommand;
import com.anseong.core.application.commands.image.UploadImageCommand;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.user.UserId;

public interface ImageCommandRepositoryService {
     void save(UploadImageCommand command, List<ConvertMediaResponse> convertMediaResponses, UserId storeId, String nickname);

	void update(UpdateImageCommand command, UserId storeId, String type);

	void remove(ImageId imageId);
}
