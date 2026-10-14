package com.anseong.core.application.queries.tag.services;

import java.util.List;

import com.anseong.core.domain.model.user.UserId;

public interface TagQueryRepositoryService {
     List<String> findCommonTagNamesByUserId(UserId userId);

	List<String> findTagNamesByKeyword(String keyword, int amount);
}
