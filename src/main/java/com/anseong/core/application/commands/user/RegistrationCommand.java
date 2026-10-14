package com.anseong.core.application.commands.user;

import java.util.List;
import java.util.Set;

public record RegistrationCommand(
     String username
     , String nickname
     , String password
     , String impUid
     , boolean isAdult
     , int imageSort
     , List<String> favoriteList
     , Set<String> boardLikeList
     , List<String> searchHistory
) {}
