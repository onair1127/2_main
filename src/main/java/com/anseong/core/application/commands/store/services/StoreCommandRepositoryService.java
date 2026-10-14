package com.anseong.core.application.commands.store.services;

import com.anseong.core.application.commands.store.MakeOwnStoreCommand;
import com.anseong.core.application.commands.store.UpdateMyStoreCommand;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;
import com.anseong.core.domain.model.user.UserId;

public interface StoreCommandRepositoryService {
	void save(UserId userId, MakeOwnStoreCommand command, ConvertMediaResponse convertedThumbnail);

	void update(UserId userId, UpdateMyStoreCommand command, ConvertMediaResponse convertedThumbnail);

     Long findDuplicatedStore(UserId userId, long bizNo);
}
