package com.anseong.core.web.payloads;

import com.anseong.core.application.commands.image.UpdateImageCommand;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateImagePayload {

     @NotNull
     @Min(value = 1, message = "imageId 값이 존재하지 않습니다.")
     private long imageId;

     @NotNull
     @NotEmpty
     @Size(max = 100, message = "제목이 너무 깁니다.")
     private String title;

     @NotNull
     private boolean job;

     @NotNull
     @Min(value = 0, message = "가격 값이 존재하지 않습니다.")
     private int price;

     @NotNull
     private String description;

     @NotNull
     private boolean isSquare;

     public UpdateImageCommand toCommand() {
          return new UpdateImageCommand(imageId, title, job, price, description, isSquare);
     }

     public void setImageId(long imageId) {
          this.imageId = imageId;
     }

     public void setTitle(String title) {
          this.title = title;
     }

     public void setJob(boolean job) {
          this.job = job;
     }

     public void setPrice(int price) {
          this.price = price;
     }

     public void setDescription(String description) {
          this.description = description;
     }

     public void setSquare(boolean isSquare) {
          this.isSquare = isSquare;
     }
}
