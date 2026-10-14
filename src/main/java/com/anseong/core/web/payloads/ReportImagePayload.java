package com.anseong.core.web.payloads;

import com.anseong.core.application.commands.report.ReportImageCommand;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ReportImagePayload {

     @NotNull
     @NotEmpty
     private long imageId;

     @NotNull
     @NotEmpty
     private String imageUrl;

     @NotNull
     @NotEmpty
     private String content;

     public ReportImageCommand toCommand() {
          return new ReportImageCommand(imageId, imageUrl, content);
     }

     public void setImageId(long imageId) {
          this.imageId = imageId;
     }

     public void setImageUrl(String imageUrl) {
          this.imageUrl = imageUrl;
     }

     public void setContent(String content) {
          this.content = content;
     }
}
