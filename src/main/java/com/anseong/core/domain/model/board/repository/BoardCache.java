package com.anseong.core.domain.model.board.repository;

import java.util.List;

import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board.response.BoardResponse;
import com.anseong.core.domain.model.user.UserId;

public interface BoardCache {

     List<BoardResponse> setBoardViewsAndLikes(List<BoardResponse> boards);

     void addBoardView(BoardId boardId);

     void addBoardLike(BoardId boardId, UserId userId);

     void removeBoardLike(BoardId boardId, UserId userId);

     void deleteBoard(BoardId boardId);
}
