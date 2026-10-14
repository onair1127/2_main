package com.anseong.core.application.queries.board.services;

import java.util.List;

import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board.response.BoardResponse;

public interface BoardQueryRepositoryService {
     BoardResponse findById(BoardId boardId);

     List<BoardResponse> find(BoardId lastBoardId, boolean isCeo, boolean isAdult, int amount);
}
