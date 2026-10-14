package com.anseong.core.application.queries.image.services;

import java.util.List;

import com.anseong.core.application.queries.image.GetImageQuery;
import com.anseong.core.application.queries.image.GetImageResponse;
import com.anseong.core.application.queries.image.MainImageQuery;
import com.anseong.core.application.queries.image.MainImageResponse;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.user.UserId;

public interface ImageQueryService {
     ImageResponse getRandomOne(boolean isAdult);

     MainImageResponse getRandomTagsAndImages(MainImageQuery query);

     GetImageResponse getImages(GetImageQuery query, boolean isAdult);

     List<ImageResponse> getImagesByType(String type, ImageId lastImageId, boolean isAdult);

	List<ImageResponse> getStoreImages(UserId storeId, ImageId lastImageId, String type, boolean isAdult);

	List<ImageResponse> getFavorites(UserId userId, List<ImageId> imageIds);

     List<ImageResponse> getUpdatableImages(UserId userId, ImageId lastImageId, boolean isAdult);
}
