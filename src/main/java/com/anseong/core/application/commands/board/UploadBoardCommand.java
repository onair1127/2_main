package com.anseong.core.application.commands.board;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.catalina.util.URLEncoder;

import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.user.UserId;

public record UploadBoardCommand(
     BoardId boardId
     , String title
     , String category
     , String contents
     , List<String> imageSrc
     , boolean isAdult
     , String createdDate
     , String selectedThumbnail
     , List<BoardStoreCommand> stores
) {
     public UploadBoardCommand(long boardId, String title, String category, String contents, List<String> imageSrc, List<Map<String, Object>> stores, boolean isAdult, String createdDate, String selectedThumbnail) {
          this(
               new BoardId(boardId)
               , URLEncoder.DEFAULT.encode(title, Charset.forName("utf-8"))
               , category
               , contents
               , imageSrc
               , isAdult
               , createdDate
               , selectedThumbnail
               , stores.stream()
                    .map(e -> {
                         String userIdString = String.valueOf(e.get("id"));
                         UserId userId = new UserId(Long.parseLong(userIdString));
                         String storeName = String.valueOf(e.get("name"));

                         return new BoardStoreCommand(userId, storeName);
                    })
                    .toList()
          );
     }
}
