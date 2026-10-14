package com.anseong.core.domain.model.image.repository;

import java.util.List;
import java.util.Map;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.user.UserId;

public interface ImageQueryRepository {
     Map<String, List<ImageId>> findIdsGroupByTagName(List<String> tagNames, String current, boolean isAdult, int amount);

     List<ImageResponse> findRandom(List<String> tagNames, List<ImageId> imageIds, String current, Boolean job, List<UserId> storeIds, boolean adult, int amount);

     List<ImageResponse> findByType(ImageId lastImageId, boolean adult, String type, int amount);

     List<ImageResponse> findByUserId(UserId userId, ImageId lastImageId, boolean isAdult, int amount);

     List<ImageResponse> findStoreImages(UserId storeId, ImageId lastImageId, String type, boolean adult, int amount);

     List<ImageResponse> findUpdatableImages(UserId userId, ImageId lastImageId, int amount);

     ImageResponse findRandomOne(boolean isAdult);

     List<ImageResponse> findByIds(List<ImageId> imageIds, boolean shuffle);

     List<ImageResponse> findByIds(List<ImageId> imageIds, String referenceTagName, boolean shuffle);

     List<ImageId> findIdsByUserId(UserId userId);
}
