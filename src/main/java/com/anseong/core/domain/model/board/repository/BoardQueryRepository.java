package com.anseong.core.domain.model.board.repository;

import java.util.List;

import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board.response.BoardResponse;

public interface BoardQueryRepository {
     List<BoardResponse> find(BoardId lastBoardId, boolean isCeo, boolean isAdult, int amount);

     BoardResponse findById(BoardId boardId);
}
