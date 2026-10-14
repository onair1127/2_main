package com.anseong.core.application.commands.user;

public record UpdateMyPasswordCommand(
     String username,
     String password,
     String impUid
) {}
