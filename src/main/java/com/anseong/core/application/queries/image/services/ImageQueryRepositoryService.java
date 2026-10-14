package com.anseong.core.application.queries.image.services;

import java.util.List;
import java.util.Map;

import com.anseong.core.application.queries.image.GetImageQuery;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.user.UserId;

public interface ImageQueryRepositoryService {
     ImageResponse findRandomOne(boolean isAdult);

     Map<String, List<ImageResponse>> findGroupByTagName(List<String> tagNames, String current, boolean isAdult, int amountPerTag);

     List<ImageResponse> findRandom(GetImageQuery query, boolean isAdult, int amount);

     List<ImageResponse> findByIds(List<ImageId> imageIds, boolean shuffle);

     List<ImageResponse> findByType(ImageId lastImageId, boolean isAdult, String type, int amount);

     List<ImageResponse> findByUserId(UserId userId, ImageId lastImageId, boolean isAdult, int amount);

	List<ImageResponse> findStoreImages(UserId storeId, ImageId lastImageId, String type, boolean isAdult, int amount);
}
