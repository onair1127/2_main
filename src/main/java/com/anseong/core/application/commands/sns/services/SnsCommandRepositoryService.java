package com.anseong.core.application.commands.sns.services;

import java.util.List;

import com.anseong.core.application.commands.sns.SnsCommand;
import com.anseong.core.domain.model.user.UserId;

public interface SnsCommandRepositoryService {
     void save(UserId userId, SnsCommand sns);

     void save(UserId storeId, List<SnsCommand> snsCommands);

     void remove(UserId userId, String type);
}
