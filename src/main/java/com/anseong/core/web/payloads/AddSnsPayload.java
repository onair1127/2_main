package com.anseong.core.web.payloads;

import com.anseong.core.application.commands.sns.AddSnsCommand;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddSnsPayload {

     @NotNull
     @Size(min = 1, message = "너무 짧습니다.")
     private String type;

     @NotNull
     @Size(min = 10, message = "너무 짧습니다.")
     private String url;

     public AddSnsCommand toCommand() {
          return new AddSnsCommand(type, url);
     }

     public void setType(String type) {
          this.type = type;
     }

     public void setUrl(String url) {
          this.url = url;
     }

}
