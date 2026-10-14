package com.anseong.core.web.payloads;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.anseong.core.application.commands.image.UploadImageCommand;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UploadImagePayload {

     @NotNull
     private List<Map<String, Object>> images;

     @NotNull
     private long storeId;

     @NotNull
     @NotEmpty
     private String type;

     @NotNull
     @NotEmpty
     private String title;

     @NotNull
     private int price;

     @NotNull
     private List<Map<String, String>> tags;

     @NotNull
     @NotEmpty
     private String description;

     private String phone;
     private boolean isJob;
     private LocalDateTime createdDate;

     public UploadImageCommand toCommand() {
          return new UploadImageCommand(images, tags, storeId, type, price, title, description, phone, isJob, createdDate);
     }

     public void setImages(List<Map<String, Object>> images) {
          this.images = images;
     }

     public void setStoreId(long storeId) {
          this.storeId = storeId;
     }

     public void setType(String type) {
          this.type = type;
     }

     public void setTitle(String title) {
          this.title = title;
     }

     public void setPrice(int price) {
          this.price = price;
     }

     public void setTags(List<Map<String, String>> tags) {
          this.tags = tags;
     }

     public void setDescription(String description) {
          this.description = description;
     }

     public void setPhone(String phone) {
          this.phone = phone;
     }

     public void setCreatedDate(LocalDateTime createdDate) {
          this.createdDate = createdDate;
     }
}
