package com.anseong.core.application.commands.store.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.commands.store.MakeOwnStoreCommand;
import com.anseong.core.application.commands.store.UpdateMyStoreCommand;
import com.anseong.core.application.commands.store.services.StoreCommandRepositoryService;
import com.anseong.core.domain.aop.semaphore.MasterDatasourceSemaphore;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;
import com.anseong.core.domain.model.sns.Sns;
import com.anseong.core.domain.model.sns.repository.SnsCommandRepository;
import com.anseong.core.domain.model.store.Store;
import com.anseong.core.domain.model.store.repository.StoreCommandRepository;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.repository.UserCommandRepository;

@Service
@Transactional
@MasterDatasourceSemaphore
class StoreCommandRepositoryServiceImpl implements StoreCommandRepositoryService {

     private final StoreCommandRepository storeCommandRepository;
     private final SnsCommandRepository snsCommandRepository;
     private final UserCommandRepository userCommandRepository;

     StoreCommandRepositoryServiceImpl(
          StoreCommandRepository storeCommandRepository,
          SnsCommandRepository snsCommandRepository,
          UserCommandRepository userCommandRepository
     ) {
          this.storeCommandRepository = storeCommandRepository;
          this.snsCommandRepository = snsCommandRepository;
          this.userCommandRepository = userCommandRepository;
     }

     @Override
	public void save(UserId userId, MakeOwnStoreCommand command, ConvertMediaResponse convertedThumbnail) {
          Store store = Store.create(userId, command, convertedThumbnail);

          storeCommandRepository.save(store);
          userCommandRepository.updateCeoInfo(userId, command.name());
	}

     @Override
	public void update(UserId userId, UpdateMyStoreCommand command, ConvertMediaResponse convertedThumbnail) {
          Store store = storeCommandRepository.findByUserId(userId);
          store.update(command, convertedThumbnail);

          List<Sns> sns = Sns.create(userId, command.snsCommands());
          snsCommandRepository.removeByUserId(userId);
          snsCommandRepository.save(sns);
	}

     @Override
     public Long findDuplicatedStore(UserId userId, long bizNo) {
          return storeCommandRepository.findDuplicatedStore(userId, bizNo);
     }
}
