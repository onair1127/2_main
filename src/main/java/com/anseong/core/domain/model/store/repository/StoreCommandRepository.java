package com.anseong.core.domain.model.store.repository;

import com.anseong.core.domain.model.store.ImageStore;
import com.anseong.core.domain.model.store.Store;
import com.anseong.core.domain.model.user.UserId;

public interface StoreCommandRepository {

	void save(Store store);

	Store findByUserId(UserId storeId);

     void remove(Store store);

     Long findDuplicatedStore(UserId storeId, long bizNo);

     ImageStore getImageStoreReferenceById(UserId userId);
}
