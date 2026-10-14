package com.anseong.core.web.payloads;

import java.util.List;
import java.util.Map;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.anseong.core.application.commands.board.UploadBoardCommand;

public class UploadBoardPayload {

     @Nullable
     private long boardId;

     @NotNull
     @NotEmpty
     private String title;

     @NotNull
     @NotEmpty
     private String category;

     @NotNull
     @NotEmpty
     private String contents;

     @NotNull
     private List<String> imageSrc;

     @NotNull
     private List<Map<String, Object>> stores;

     @NotNull
     private boolean adult;

     @NotNull
     @NotEmpty
     private String createdDate;

     private String selectedThumbnail;

     public UploadBoardCommand toCommand() {
          return new UploadBoardCommand(boardId, title, category, contents, imageSrc, stores, adult, createdDate, selectedThumbnail);
     }

     public void setBoardId(long boardId) {
          this.boardId = boardId;
     }

     public void setTitle(String title) {
          this.title = title;
     }

     public void setCategory(String category) {
          this.category = category;
     }

     public void setContent(String contents) {
          this.contents = contents;
     }

     public void setImageSrc(List<String> imageSrc) {
          this.imageSrc = imageSrc;
     }

     public void setStores(List<Map<String, Object>> stores) {
          this.stores = stores;
     }

     public void setAdult(boolean adult) {
          this.adult = adult;
     }

     public void setCreatedDate(String createdDate) {
          this.createdDate = createdDate;
     }

     public void setSelectedThumbnail(String selectedThumbnail) {
          this.selectedThumbnail = selectedThumbnail;
     }
}
