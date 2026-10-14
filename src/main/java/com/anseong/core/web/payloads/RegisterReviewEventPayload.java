package com.anseong.core.web.payloads;

import com.anseong.core.application.commands.event.RegisterReviewEventCommand;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class RegisterReviewEventPayload {

     @NotNull
     @NotEmpty
     private String studentId;

     private String image;

     private String description;

     public RegisterReviewEventCommand toCommand() {
          return new RegisterReviewEventCommand(studentId, image, description);
     }

     public void setStudentId(String studentId) {
          this.studentId = studentId;
     }

     public void setImage(String image) {
          this.image = image;
     }

     public void setDescription(String description) {
          this.description = description;
     }
}
