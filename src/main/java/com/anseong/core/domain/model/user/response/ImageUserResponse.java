package com.anseong.core.domain.model.user.response;

import com.anseong.core.domain.model.user.ImageUser;

public record ImageUserResponse(
     String nickname,
     String profile
) {
     public static ImageUserResponse from(ImageUser reviewUser) {
          return new ImageUserResponse(reviewUser.getNickname(), reviewUser.getProfile());
     }
}
