package com.anseong.core.application.commands.image;

public record ImageCommand(
     String url,
     String filename,
     String ext,
     String filter,
     int sequence,
     boolean isSquare
) {}
