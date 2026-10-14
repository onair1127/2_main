package com.anseong.core.application.commands.image.services;

import com.anseong.core.application.commands.image.DeleteImageCommand;
import com.anseong.core.application.commands.image.UpdateImageCommand;
import com.anseong.core.application.commands.image.UploadImageCommand;
import com.anseong.core.domain.model.user.UserId;

public interface ImageCommandService {

	void uploadImage(UploadImageCommand command, UserId storeId, String nickname, String type);

	void deleteImage(DeleteImageCommand command);

	void updateImage(UpdateImageCommand command, UserId storeId, String type);
}
