package com.anseong.core.application.commands.sns.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.anseong.core.application.commands.sns.AddSnsCommand;
import com.anseong.core.application.commands.sns.AdminAddSnsCommand;
import com.anseong.core.application.commands.sns.services.SnsCommandRepositoryService;
import com.anseong.core.application.commands.sns.services.SnsCommandService;
import com.anseong.core.domain.model.user.UserId;

@Service
class SnsCommandServiceImpl implements SnsCommandService {

     private final SnsCommandRepositoryService snsCommandRepositoryService;

     SnsCommandServiceImpl(SnsCommandRepositoryService snsCommandRepositoryService) {
          this.snsCommandRepositoryService = snsCommandRepositoryService;
     }

     @Override
	public void delete(String type, UserId userId) {
          Assert.hasText(type, "가게 서비스 : 파라미터 `type`가 비어있습니다");
          Assert.notNull(userId, "가게 서비스 : 파라미터 `userId`가 비어있습니다.");

          snsCommandRepositoryService.remove(userId, type);
	}

	@Override
	public void add(AddSnsCommand command, UserId userId) {
          Assert.notNull(command, "가게 서비스 : 파라미터 `command`가 비어있습니다.");
          Assert.notNull(command, "가게 서비스 : 파라미터 `userId`가 비어있습니다.");

          snsCommandRepositoryService.save(userId, command.snsCommand());
	}

     @Override
     public void addByAdmin(AdminAddSnsCommand command) {
          snsCommandRepositoryService.save(command.storeId(), command.snsCommands());
     }
}
