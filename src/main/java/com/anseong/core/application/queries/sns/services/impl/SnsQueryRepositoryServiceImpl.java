package com.anseong.core.application.queries.sns.services.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.queries.sns.services.SnsQueryRepositoryService;
import com.anseong.core.domain.aop.semaphore.ReplicaDatasourceSemaphore;
import com.anseong.core.domain.model.sns.repository.SnsQueryRepository;
import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.user.UserId;

@Service
@Transactional(readOnly = true)
@ReplicaDatasourceSemaphore
public class SnsQueryRepositoryServiceImpl implements SnsQueryRepositoryService {

     private final SnsQueryRepository snsQueryRepository;

     public SnsQueryRepositoryServiceImpl(SnsQueryRepository snsQueryRepository) {
          this.snsQueryRepository = snsQueryRepository;
     }

     @Override
     public List<SnsResponse> findByUserIds(Collection<UserId> userIds) {
          return snsQueryRepository.findByUserIds(userIds);
     }
}
