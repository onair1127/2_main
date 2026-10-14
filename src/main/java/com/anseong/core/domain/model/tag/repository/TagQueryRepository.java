package com.anseong.core.domain.model.tag.repository;

import java.util.Collection;
import java.util.List;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.tag.response.StoreTagResponse;
import com.anseong.core.domain.model.tag.response.TagResponse;
import com.anseong.core.domain.model.user.UserId;

public interface TagQueryRepository {
     List<TagResponse> findByImageId(ImageId id);

     List<TagResponse> findByImageIds(Collection<ImageId> imageIds);

     List<StoreTagResponse> findStoreTagsByUserId(UserId userId);

     List<StoreTagResponse> findStoreTagsByUserIds(Collection<UserId> userIds);

     List<String> findCommonTagNamesByUserId(UserId userId);

	List<String> findTagNamesByKeyword(String keyword, int amount);
}
