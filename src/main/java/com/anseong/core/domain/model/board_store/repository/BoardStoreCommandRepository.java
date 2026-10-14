package com.anseong.core.domain.model.board_store.repository;

import java.util.List;

import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board_store.BoardStore;
import com.anseong.core.domain.model.user.UserId;

public interface BoardStoreCommandRepository {

     void save(List<BoardStore> boardStores);

     void save(BoardStore boardStore);

     void remove(List<BoardStore> boardStores);

     void removeByBoardId(BoardId id);

     void removeByBoardIdAndUserId(BoardId boardId, UserId userId);
}
