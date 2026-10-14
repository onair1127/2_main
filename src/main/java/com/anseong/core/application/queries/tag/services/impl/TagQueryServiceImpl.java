package com.anseong.core.application.queries.tag.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.anseong.core.application.queries.tag.services.TagQueryRepositoryService;
import com.anseong.core.application.queries.tag.services.TagQueryService;
import com.anseong.core.domain.model.user.UserId;

@Service
public class TagQueryServiceImpl implements TagQueryService {

     private final TagQueryRepositoryService tagQueryRepositoryService;

     public TagQueryServiceImpl(TagQueryRepositoryService tagQueryRepositoryService) {
          this.tagQueryRepositoryService = tagQueryRepositoryService;
     }

     @Override
     public List<String> getCommonTagNamesByUserId(UserId userId) {
          Assert.notNull(userId, "파라미터 `id` 값은 항상 존재하여야 합니다.");

          return tagQueryRepositoryService.findCommonTagNamesByUserId(userId);
     }

     @Override
     public List<String> getTagNames(String keyword) {
          return tagQueryRepositoryService.findTagNamesByKeyword(keyword, 5);
     }
}
