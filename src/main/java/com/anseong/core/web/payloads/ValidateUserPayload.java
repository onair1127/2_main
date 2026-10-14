package com.anseong.core.web.payloads;

import com.anseong.core.application.queries.user.ValidateUserQuery;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ValidateUserPayload {

     @NotNull
     @NotEmpty
     private String username;

     @NotNull
     @NotEmpty
     private String nickname;

     public ValidateUserQuery toCommand() {
          return new ValidateUserQuery(username, nickname);
     }

     public void setUsername(String username) {
          this.username = username;
     }

     public void setNickname(String nickname) {
          this.nickname = nickname;
     }
}
