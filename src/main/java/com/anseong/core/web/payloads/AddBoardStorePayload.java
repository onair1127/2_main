package com.anseong.core.web.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.anseong.core.application.commands.board.AddBoardStoreCommand;

public class AddBoardStorePayload {

     @NotNull
     private long boardId;

     @NotNull
     private long storeId;

     @NotNull
     @NotEmpty
     private String storeName;

     public AddBoardStoreCommand toCommand() {
          return new AddBoardStoreCommand(boardId, storeId, storeName);
     }

     public void setBoardId(long boardId) {
          this.boardId = boardId;
     }

     public void setStoreId(long storeId) {
          this.storeId = storeId;
     }

     public void setStoreName(String storeName) {
          this.storeName = storeName;
     }
}
