package com.anseong.core.application.commands.event.services.impl;

import org.springframework.stereotype.Service;

import com.anseong.core.application.commands.event.RegisterReviewEventCommand;
import com.anseong.core.application.commands.event.services.EventCommandRepositoryService;
import com.anseong.core.application.commands.event.services.EventCommandService;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.exception.EventAlreadyRegisteredException;

@Service
class EventCommandServiceImpl implements EventCommandService {

     private final EventCommandRepositoryService eventCommandRepositoryService;

     EventCommandServiceImpl(
          EventCommandRepositoryService eventCommandRepositoryService
     ) {
          this.eventCommandRepositoryService = eventCommandRepositoryService;
     }

     @Override
     public void registerReviewEvent(UserId userId, RegisterReviewEventCommand command) throws EventAlreadyRegisteredException {
          eventCommandRepositoryService.saveReviewEvent(userId, command);
     }

     @Override
     public boolean validate(UserId userId) {
          return eventCommandRepositoryService.existsByUserId(userId);
     }
}
