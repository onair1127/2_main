package com.anseong.core.application.commands.user;

public record UpdateMyProfileCommand(
     String nickname,
     String profile,
     String logo
) {}
