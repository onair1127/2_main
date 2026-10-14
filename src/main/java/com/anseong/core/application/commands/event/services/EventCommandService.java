package com.anseong.core.application.commands.event.services;

import com.anseong.core.application.commands.event.RegisterReviewEventCommand;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.exception.EventAlreadyRegisteredException;

public interface EventCommandService {
     void registerReviewEvent(UserId userId, RegisterReviewEventCommand command) throws EventAlreadyRegisteredException;

     boolean validate(UserId userId);
}
