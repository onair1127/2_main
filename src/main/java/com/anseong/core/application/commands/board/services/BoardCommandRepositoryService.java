package com.anseong.core.application.commands.board.services;

import com.anseong.core.application.commands.board.AddBoardStoreCommand;
import com.anseong.core.application.commands.board.UploadBoardCommand;
import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.user.UserId;

public interface BoardCommandRepositoryService {
     BoardId createBoard(UploadBoardCommand command, UserId userId, String thumbnailImage, String boardContents);

     void updateBoard(UploadBoardCommand command, UserId userId, String thumbnailImage, String boardContents);

     void deleteBoard(BoardId boardId);

     void deleteBoardStore(BoardId boardId, UserId storeId);

     void addBoardStore(AddBoardStoreCommand command);
}
