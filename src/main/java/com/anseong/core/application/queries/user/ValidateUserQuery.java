package com.anseong.core.application.queries.user;

public record ValidateUserQuery(
     String username,
     String nickname
) {
     public ValidateUserQuery(String username, String nickname) {
          this.username = username;
          this.nickname = nickname;
     }
}
