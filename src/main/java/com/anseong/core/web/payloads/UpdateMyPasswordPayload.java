package com.anseong.core.web.payloads;

import com.anseong.core.application.commands.user.UpdateMyPasswordCommand;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UpdateMyPasswordPayload {

     @NotNull
     @NotEmpty
     private String username;

     @NotNull
     @NotEmpty
     private String password;

     @NotNull
     @NotEmpty
     private String impUid;

     public UpdateMyPasswordCommand toCommand() {
          return new UpdateMyPasswordCommand(username, password, impUid);
     }

     public void setUsername(String username) {
          this.username = username;
     }

     public void setPassword(String password) {
          this.password = password;
     }

     public void setImpUid(String impUid) {
          this.impUid = impUid;
     }
}
