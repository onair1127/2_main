package com.anseong.core.web.payloads;

import com.anseong.core.application.commands.user.UpdateMyProfileCommand;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UpdateMyProfilePayload {

     @NotNull
     @NotEmpty
     private String nickname;

     private String profile;

     private String logo;

     public UpdateMyProfileCommand toCommand() {
          return new UpdateMyProfileCommand(nickname, profile, logo);
     }

     public void setNickname(String nickname) {
          this.nickname = nickname;
     }

     public void setProfile(String profile) {
          this.profile = profile;
     }

     public void setLogo(String logo) {
          this.logo = logo;
     }
}
