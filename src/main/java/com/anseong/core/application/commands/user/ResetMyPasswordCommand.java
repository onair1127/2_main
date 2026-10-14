package com.anseong.core.application.commands.user;

public record ResetMyPasswordCommand(
     String username,
     String impUid
) {}
