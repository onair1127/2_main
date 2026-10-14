package com.anseong.core.domain.model.image.repository;

import java.util.List;

import com.anseong.core.domain.model.image.Image;
import com.anseong.core.domain.model.image.ImageId;

public interface ImageCommandRepository {

     Image findById(ImageId imageId);

     Image getReferenceById(ImageId imageId);

	void save(Image result);

	void remove(Image image);

	void removeById(ImageId imageId);

     void save(List<Image> Images);

	ImageId saveAndReturn(Image image);
}
