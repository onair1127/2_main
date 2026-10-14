package com.anseong.core.application.commands.event;

public record RegisterReviewEventCommand(
     String studentId,
     String image,
     String description
) {}
