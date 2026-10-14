package com.anseong.core.domain.model.tag.repository;

import java.util.List;

import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.tag.Tag;
import com.anseong.core.domain.model.user.UserId;

public interface TagCommandRepository {

	void save(List<Tag> tags);

	List<Tag> findByImageId(ImageId imageId);

	List<Tag> findByUserId(UserId userId);

	// void remove(List<Tag> tags);

	void deleteByUserIdAndTagName(UserId userId, String tagName);

	void deleteByImageIdAndTagName(ImageId imageId, String tagName);

	void remove(Tag deleteTag);

	Tag findPersonalTagByImageIdAndUserId(ImageId imageId, UserId userId, String tagName);

	void save(Tag newTag);
}
