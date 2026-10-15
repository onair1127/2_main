package com.anseong.core.application.queries.tag.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.queries.tag.services.TagQueryRepositoryService;
import com.anseong.core.domain.aop.semaphore.ReplicaDatasourceSemaphore;
import com.anseong.core.domain.model.tag.repository.TagQueryRepository;
import com.anseong.core.domain.model.user.UserId;

@Service
@Transactional(readOnly = true)
@ReplicaDatasourceSemaphore
class TagQueryRepositoryServiceImpl implements TagQueryRepositoryService {

     private final TagQueryRepository tagQueryRepository;

     TagQueryRepositoryServiceImpl(TagQueryRepository tagQueryRepository) {
          this.tagQueryRepository = tagQueryRepository;
     }

     @Override
     public List<String> findCommonTagNamesByUserId(UserId userId) {
          return tagQueryRepository.findCommonTagNamesByUserId(userId);
     }

     @Override
     public List<String> findTagNamesByKeyword(String keyword, int amount) {
          return tagQueryRepository.findTagNamesByKeyword(keyword, amount);
     }
}
