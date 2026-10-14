package com.anseong.core.web.payloads;

import com.anseong.core.application.commands.tag.AddPersonalTagCommand;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddPersonalTagPayload {

     @NotNull
     @NotEmpty
     @Size(max = 6, message = "사이즈가 너무 큽니다.")
     private String tag;

     @NotNull
     private long imageId;

     public AddPersonalTagCommand toCommand() {
          return new AddPersonalTagCommand(imageId, tag);
     }

     public void setTag(String tag) {
          this.tag = tag;
     }

     public void setImageId(long imageId) {
          this.imageId = imageId;
     }
}
