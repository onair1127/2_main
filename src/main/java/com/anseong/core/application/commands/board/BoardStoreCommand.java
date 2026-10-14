package com.anseong.core.application.commands.board;

import com.anseong.core.domain.model.user.UserId;

public record BoardStoreCommand(
     UserId userId,
     String storeName
) {}
