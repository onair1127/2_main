package com.anseong.core.web.payloads;

import java.util.List;

import com.anseong.core.application.commands.tag.AddCommonTagCommand;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AddCommonTagPayload {

     @NotNull
     @NotEmpty
     private String tag;

     @NotNull
     private List<Long> imageIds;

     public AddCommonTagCommand toCommand() {
          return AddCommonTagCommand.of(tag, imageIds);
     }

     public void setTag(String tag) {
          this.tag = tag;
     }

     public void setImageIds(List<Long> imageIds) {
          this.imageIds = imageIds;
     }
}
