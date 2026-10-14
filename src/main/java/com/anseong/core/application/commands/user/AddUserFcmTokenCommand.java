package com.anseong.core.application.commands.user;

public record AddUserFcmTokenCommand(
     String token,
     String OS
) {}
