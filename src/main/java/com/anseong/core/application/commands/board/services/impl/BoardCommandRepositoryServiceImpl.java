package com.anseong.core.application.commands.board.services.impl;

import org.springframework.stereotype.Service;

import com.anseong.core.application.commands.board.AddBoardStoreCommand;
import com.anseong.core.application.commands.board.UploadBoardCommand;
import com.anseong.core.application.commands.board.services.BoardCommandRepositoryService;
import com.anseong.core.domain.aop.semaphore.MasterDatasourceSemaphore;
import com.anseong.core.domain.model.board.Board;
import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board.repository.BoardCommandRepository;
import com.anseong.core.domain.model.board_store.BoardStore;
import com.anseong.core.domain.model.board_store.repository.BoardStoreCommandRepository;
import com.anseong.core.domain.model.user.UserId;

import jakarta.transaction.Transactional;

@Service
@Transactional
@MasterDatasourceSemaphore
class BoardCommandRepositoryServiceImpl implements BoardCommandRepositoryService {

     private final BoardCommandRepository boardCommandRepository;
     private final BoardStoreCommandRepository boardStoreCommandRepository;

     BoardCommandRepositoryServiceImpl(
          BoardCommandRepository boardCommandRepository,
          BoardStoreCommandRepository boardStoreCommandRepository
     ) {
          this.boardCommandRepository = boardCommandRepository;
          this.boardStoreCommandRepository = boardStoreCommandRepository;
     }

     @Override
     public BoardId createBoard(UploadBoardCommand command, UserId userId, String thumbnailImage, String boardContents) {
          Board board = Board.create(command, userId, thumbnailImage, boardContents);
          boardCommandRepository.save(board);

          return board.getId();
     }

     @Override
     public void updateBoard(UploadBoardCommand command, UserId userId, String thumbnailImage, String boardContents) {
          Board board = boardCommandRepository.findById(command.boardId());

          board.update(command, userId, thumbnailImage, boardContents);
     }

     @Override
     public void deleteBoard(BoardId boardId) {
          Board board = boardCommandRepository.findById(boardId);

          boardCommandRepository.remove(board);
     }

     @Override
     public void deleteBoardStore(BoardId boardId, UserId storeId) {
          boardStoreCommandRepository.removeByBoardIdAndUserId(boardId, storeId);
     }

     @Override
     public void addBoardStore(AddBoardStoreCommand command) {
          Board proxyBoard = boardCommandRepository.getReferenceById(command.boardId());
          BoardStore boardStore = BoardStore.create(proxyBoard, command.storeId(), command.storeName());

          boardStoreCommandRepository.save(boardStore);
     }
}
