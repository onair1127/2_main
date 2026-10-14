package com.anseong.core.domain.model.board.repository;

import com.anseong.core.domain.model.board.Board;
import com.anseong.core.domain.model.board.BoardId;

public interface BoardCommandRepository {

     Board findById(BoardId boardId);

     Board getReferenceById(BoardId boardId);

	void save(Board newBoard);

     void remove(Board board);
}
