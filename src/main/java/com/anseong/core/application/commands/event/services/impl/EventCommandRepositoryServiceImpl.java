package com.anseong.core.application.commands.event.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.commands.event.RegisterReviewEventCommand;
import com.anseong.core.application.commands.event.services.EventCommandRepositoryService;
import com.anseong.core.domain.aop.semaphore.MasterDatasourceSemaphore;
import com.anseong.core.domain.model.event.Event;
import com.anseong.core.domain.model.event.repository.EventCommandRepository;
import com.anseong.core.domain.model.event.repository.EventQueryRepository;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.exception.EventAlreadyRegisteredException;

@Service
@Transactional
@MasterDatasourceSemaphore
class EventCommandRepositoryServiceImpl implements EventCommandRepositoryService {

     private final EventCommandRepository eventCommandRepository;
     private final EventQueryRepository eventQueryRepository;

     EventCommandRepositoryServiceImpl(
          EventCommandRepository eventCommandRepository,
          EventQueryRepository eventQueryRepository
     ) {
          this.eventCommandRepository = eventCommandRepository;
          this.eventQueryRepository = eventQueryRepository;
     }

     @Override
     public void saveReviewEvent(UserId userId, RegisterReviewEventCommand command) {
          boolean alreadyRegistered = eventQueryRepository.existsByUserId(userId);

          if (alreadyRegistered)
               throw new EventAlreadyRegisteredException("이미 신청됨.");

          Event event = Event.create(userId, command);
          eventCommandRepository.save(event);
     }

     @Override
     public boolean existsByUserId(UserId userId) {
          return eventQueryRepository.existsByUserId(userId);
     }
}
