package com.anseong.core.application.commands.board;

import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.user.UserId;

public record AddBoardStoreCommand(
     BoardId boardId,
     UserId storeId,
     String storeName
) {
     public AddBoardStoreCommand(
          long boardId,
          long storeId,
          String storeName
     ) {
          this(
               new BoardId(boardId),
               new UserId(storeId),
               storeName
          );
     }
}
