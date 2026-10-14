package com.anseong.core.domain.model.board.response;

import java.time.LocalDateTime;
import java.util.List;

import com.anseong.core.domain.model.board.Board;
import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board_store.response.BoardStoreResponse;
import com.anseong.core.domain.model.user.UserId;

public record BoardResponse(
     BoardId id,
     UserId userId,
     String thumbnail,
     String title,
     String category,
     String contents,
     boolean adult,
     LocalDateTime createdDate,
     List<BoardStoreResponse> boardStores,
     int views,
     int likes
) {
     public BoardResponse(
          long id,
          long userId,
          String thumbnail,
          String title,
          String category,
          String contents,
          boolean adult,
          LocalDateTime createdDate
     ) {
          this(
               new BoardId(id)
               , new UserId(userId)
               , thumbnail
               , title
               , category
               , contents
               , adult
               , createdDate
               , null
               , 0
               , 0
          );
     }

     public static BoardResponse from(Board board) {
          return new BoardResponse(
               board.getId()
               , board.getUserId()
               , board.getThumbnail()
               , board.getTitle()
               , board.getCategory()
               , board.getContents()
               , board.isAdult()
               , board.getCreatedDate()
               , BoardStoreResponse.from(board.getBoardStores())
               , 0
               , 0
          );
     }

     public BoardResponse withViewsAndLikes(int views, int likes) {
          return new BoardResponse(
               this.id
               , this.userId
               , this.thumbnail
               , this.title
               , this.category
               , this.contents
               , this.adult
               , this.createdDate
               , this.boardStores
               , views
               , likes
          );
     }
}
