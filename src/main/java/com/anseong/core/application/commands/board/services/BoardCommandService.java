package com.anseong.core.application.commands.board.services;

import com.anseong.core.application.commands.board.AddBoardStoreCommand;
import com.anseong.core.application.commands.board.UploadBoardCommand;
import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.user.UserId;

public interface BoardCommandService {

	void createBoard(UploadBoardCommand command, UserId userId);

     void updateBoard(UploadBoardCommand command, UserId userId);

     void deleteBoard(BoardId boardId);

     void deleteBoardStore(BoardId boardId, UserId storeId);

     void addBoardStore(AddBoardStoreCommand command);
}
