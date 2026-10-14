package com.anseong.core.domain.model.multiple_image.repository;

import java.util.Collection;
import java.util.List;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.multiple_image.response.MultipleImageResponse;

public interface MultipleImageQueryRepository {
     List<MultipleImageResponse> findByImageIds(Collection<ImageId> imageIds);

     List<MultipleImageResponse> findByImageId(ImageId imageId);
}
