package com.anseong.core.application.queries.image;

public record MainImageQuery(
     String current,
     boolean isAdult,
     Double lat,
     Double lng
) {}

