package com.anseong.core.application.commands.sns.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.commands.sns.SnsCommand;
import com.anseong.core.application.commands.sns.services.SnsCommandRepositoryService;
import com.anseong.core.domain.aop.semaphore.MasterDatasourceSemaphore;
import com.anseong.core.domain.model.sns.Sns;
import com.anseong.core.domain.model.sns.repository.SnsCommandRepository;
import com.anseong.core.domain.model.user.UserId;

@Service
@Transactional
@MasterDatasourceSemaphore
class SnsCommandRepositoryServiceImpl implements SnsCommandRepositoryService {

     private final SnsCommandRepository snsCommandRepository;

     SnsCommandRepositoryServiceImpl(
          SnsCommandRepository snsCommandRepository
     ) {
          this.snsCommandRepository = snsCommandRepository;
     }

     @Override
     public void save(UserId userId, SnsCommand snsCommand) {
          Sns sns = Sns.create(userId, snsCommand);

          snsCommandRepository.save(sns);
     }

     @Override
     public void save(UserId storeId, List<SnsCommand> snsCommands) {
          List<Sns> sns = Sns.create(storeId, snsCommands);

          snsCommandRepository.save(sns);
     }

     @Override
     public void remove(UserId userId, String type) {
          snsCommandRepository.removeByUserIdAndType(userId, type);
     }
}
