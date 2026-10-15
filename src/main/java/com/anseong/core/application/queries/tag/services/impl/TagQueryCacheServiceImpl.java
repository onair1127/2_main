package com.anseong.core.application.queries.tag.services.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.anseong.core.application.queries.tag.services.TagQueryCacheService;
import com.anseong.core.domain.model.tag.repository.TagCache;

@Service
class TagQueryCacheServiceImpl implements TagQueryCacheService {
     private final TagCache tagCache;

     TagQueryCacheServiceImpl(
          TagCache tagCache
     ) {
          this.tagCache = tagCache;
     }

     public Map<String, String> getRandomRecommendTags() {
          return tagCache.getRandomRecommendTags();
     }
}
