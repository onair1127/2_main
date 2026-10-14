package com.anseong.core.application.commands.event.services;

import com.anseong.core.application.commands.event.RegisterReviewEventCommand;
import com.anseong.core.domain.model.user.UserId;

public interface EventCommandRepositoryService {
     void saveReviewEvent(UserId userId, RegisterReviewEventCommand command);

     boolean existsByUserId(UserId userId);
}
