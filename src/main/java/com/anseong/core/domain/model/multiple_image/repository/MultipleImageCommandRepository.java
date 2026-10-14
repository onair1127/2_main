package com.anseong.core.domain.model.multiple_image.repository;

import java.util.List;

import com.anseong.core.domain.model.multiple_image.MultipleImage;
import com.anseong.core.domain.model.multiple_image.MultipleImageId;
import com.anseong.core.domain.model.store.exception.StoreSaveFailedException;

public interface MultipleImageCommandRepository {

	void save(List<MultipleImage> results) throws StoreSaveFailedException;

	void remove(List<MultipleImage> images);

	void removeById(List<MultipleImageId> multipleImageIds);
}
