package com.anseong.core.web.payloads;

import java.util.List;
import java.util.Map;

import com.anseong.core.application.commands.sns.AdminAddSnsCommand;

import jakarta.validation.constraints.NotNull;

public class AdminAddSnsPayload {

     @NotNull
     private long storeId;

     @NotNull
     private List<Map<String, String>> snsData;

     public AdminAddSnsCommand toCommand() {
          return new AdminAddSnsCommand(storeId, snsData);
     }

     public void setStoreId(long storeId) {
          this.storeId = storeId;
     }

     public void setSnsData(List<Map<String, String>> snsData) {
          this.snsData = snsData;
     }
}
