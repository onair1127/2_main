package com.anseong.core.web.payloads;

import com.anseong.core.application.commands.user.ResetMyPasswordCommand;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ResetMyPasswordPayload {

     @NotNull
     @NotEmpty
     private String username;

     @NotNull
     @NotEmpty
     private String impUid;

     public ResetMyPasswordCommand toCommand() {
          return new ResetMyPasswordCommand(username, impUid);
     }

     public void setUsername(String username) {
          this.username = username;
     }

     public void setImpUid(String impUid) {
          this.impUid = impUid;
     }
}
