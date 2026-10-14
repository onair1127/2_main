package com.anseong.core.web.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AddRecommendTagPayload {

     @NotNull
     @NotEmpty
     private String tag;

     @NotNull
     @NotEmpty
     private String description;

     public void setTag(String tag) {
          this.tag = tag;
     }

     public void setDescription(String description) {
          this.description = description;
     }

     public String getTag() {
          return this.tag;
     }

     public String getDescription() {
          return this.description;
     }
}
