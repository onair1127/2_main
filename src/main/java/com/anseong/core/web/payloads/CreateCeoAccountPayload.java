package com.anseong.core.web.payloads;

import com.anseong.core.application.commands.user.CreateCeoAccountCommand;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateCeoAccountPayload {

     @NotNull
     @NotEmpty
     @Size(min = 6, max = 20, message = "아이디는 6자 이상 20자 이하이어야 합니다.")
     private String username;

     @NotNull
     @NotEmpty
     @Size(min = 2, max = 16, message = "닉네임 범위를 지켜주세요(영어: 4~16자, 한글: 2~8자)")
     private String nickname;

     @NotNull
     @NotEmpty
     @Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하이어야 합니다.")
     private String password;

     public CreateCeoAccountCommand toCommand() {
          return new CreateCeoAccountCommand(username, nickname, password);
     }

     public void setUsername(String username) {
          this.username = username;
     }

     public void setNickname(String nickname) {
          this.nickname = nickname;
     }

     public void setPassword(String password) {
          this.password = password;
     }
}
