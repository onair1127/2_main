package com.anseong.core.application.queries.board.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.anseong.core.application.queries.board.services.BoardQueryRepositoryService;
import com.anseong.core.application.queries.board.services.BoardQueryService;
import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board.repository.BoardCache;
import com.anseong.core.domain.model.board.response.BoardResponse;

@Service
class BoardQueryServiceImpl implements BoardQueryService {

     private final BoardQueryRepositoryService boardQueryRepositoryService;
     private final BoardCache boardCache;

     BoardQueryServiceImpl(BoardQueryRepositoryService boardQueryRepositoryService, BoardCache boardCache) {
          this.boardQueryRepositoryService = boardQueryRepositoryService;
          this.boardCache = boardCache;
     }

     @Override
     public BoardResponse getBoard(BoardId boardId) {
          return boardQueryRepositoryService.findById(boardId);
     }

     @Override
     public List<BoardResponse> getBoards(BoardId lastBoardId, boolean isCeo, boolean isAdult) {
          List<BoardResponse> boards = boardQueryRepositoryService.find(lastBoardId, isCeo, isAdult, 10);
          boards = boardCache.setBoardViewsAndLikes(boards);

          return boards;
     }
}
