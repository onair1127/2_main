package com.anseong.core.web.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.anseong.core.application.commands.user.AddUserFcmTokenCommand;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddUserFcmTokenPayload {

     @NotNull
     @NotEmpty
     private String token;

     @NotNull
     @NotEmpty
     @JsonProperty("OS")
     private String OS;

     public AddUserFcmTokenCommand toCommand() {
          return new AddUserFcmTokenCommand(token, OS);
     }

     public void setToken(String token) {
          this.token = token;
     }

     public void setOS(String OS) {
          this.OS = OS;
     }
}
