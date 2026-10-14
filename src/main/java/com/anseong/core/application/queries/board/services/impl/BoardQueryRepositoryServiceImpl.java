package com.anseong.core.application.queries.board.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anseong.core.application.queries.board.services.BoardQueryRepositoryService;
import com.anseong.core.domain.aop.semaphore.ReplicaDatasourceSemaphore;
import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board.repository.BoardQueryRepository;
import com.anseong.core.domain.model.board.response.BoardResponse;

@Service
@Transactional(readOnly = true)
@ReplicaDatasourceSemaphore
class BoardQueryRepositoryServiceImpl implements BoardQueryRepositoryService {

     private final BoardQueryRepository boardQueryRepository;

     BoardQueryRepositoryServiceImpl(BoardQueryRepository boardQueryRepository) {
          this.boardQueryRepository = boardQueryRepository;
     }

     @Override
     public BoardResponse findById(BoardId boardId) {
          return boardQueryRepository.findById(boardId);
     }

     @Override
     public List<BoardResponse> find(BoardId lastBoardId, boolean isCeo, boolean isAdult, int amount) {
          return boardQueryRepository.find(lastBoardId, isCeo, isAdult, amount);
     }
}
