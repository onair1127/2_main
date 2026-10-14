package com.anseong.core.web.payloads;

import com.anseong.core.application.commands.report.ReportServiceCommand;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReportServicePayload {

     @Nullable
     private String image;

     @Nullable
     private String phone;

     @NotNull
     @NotEmpty
     @Size(max = 200, message = "신고 내용은 최대 200자까지 가능합니다.")
     private String content;

     public ReportServiceCommand toCommand() {
          return new ReportServiceCommand(image, phone, content);
     }

     public void setImage(String image) {
          this.image = image;
     }

     public void setPhone(String phone) {
          this.phone = phone;
     }

     public void setContent(String content) {
          this.content = content;
     }
}
