package com.anseong.core.application.queries.board.services;

import java.util.List;

import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board.response.BoardResponse;

public interface BoardQueryService {
	BoardResponse getBoard(BoardId boardId);

	List<BoardResponse> getBoards(BoardId lastBoardId, boolean isCeo, boolean isAdult);
}
