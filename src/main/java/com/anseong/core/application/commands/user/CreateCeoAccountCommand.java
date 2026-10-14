package com.anseong.core.application.commands.user;

public record CreateCeoAccountCommand(
     String username,
     String nickname,
     String password
) {}
