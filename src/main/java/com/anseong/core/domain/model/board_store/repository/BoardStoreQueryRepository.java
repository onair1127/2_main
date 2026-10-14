package com.anseong.core.domain.model.board_store.repository;

import java.util.List;

import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board_store.response.BoardStoreResponse;

public interface BoardStoreQueryRepository {
     List<BoardStoreResponse> findByBoardId(BoardId boardId);

     List<BoardStoreResponse> findByBoardId(List<BoardId> boardIds);
}
