package com.anseong.core.domain.model.board_store.response;

import java.util.Collection;
import java.util.List;

import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board_store.BoardStore;
import com.anseong.core.domain.model.user.UserId;

public record BoardStoreResponse(
     UserId userId,
     BoardId boardId,
     String storeName
) {
     public BoardStoreResponse(
          Long userId,
          Long boardId,
          String storeName
     ) {
          this(
               new UserId(userId),
               new BoardId(boardId),
               storeName
          );
     }

     public static BoardStoreResponse from(BoardStore boardStore) {
          return new BoardStoreResponse(
               boardStore.getUserId(),
               boardStore.getBoardId(),
               boardStore.getStoreName()
          );
     }

     public static List<BoardStoreResponse> from(Collection<BoardStore> boardStores) {
          return boardStores.stream()
               .map(BoardStoreResponse::from)
               .toList();
     }
}
