package com.anseong.core.application.queries.tag.services;

import java.util.List;

import com.anseong.core.domain.model.user.UserId;

public interface TagQueryService {
     List<String> getCommonTagNamesByUserId(UserId userId);

	List<String> getTagNames(String keyword);
}
