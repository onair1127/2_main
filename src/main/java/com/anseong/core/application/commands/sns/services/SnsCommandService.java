package com.anseong.core.application.commands.sns.services;

import com.anseong.core.application.commands.sns.AddSnsCommand;
import com.anseong.core.application.commands.sns.AdminAddSnsCommand;
import com.anseong.core.domain.model.user.UserId;

public interface SnsCommandService {
     void add(AddSnsCommand command, UserId userId);

     void delete(String type, UserId storeId);

     void addByAdmin(AdminAddSnsCommand command);
}
