package com.anseong.core.application.commands.store.services;

import com.anseong.core.application.commands.store.MakeOwnStoreCommand;
import com.anseong.core.application.commands.store.UpdateMyStoreCommand;
import com.anseong.core.domain.model.store.exception.StoreDuplicatedException;
import com.anseong.core.domain.model.user.UserId;

public interface StoreCommandService {

     void validateStore(UserId userId, long bizNo) throws StoreDuplicatedException;

	void createStore(MakeOwnStoreCommand command, UserId userId, String nickname);

	void updateStore(UpdateMyStoreCommand command, String nickname, UserId userId);
}
