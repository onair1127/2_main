package com.anseong.core.application.commands.board.services.impl;

import java.util.List;
import java.util.Set;

import com.anseong.core.application.commands.board.AddBoardStoreCommand;
import com.anseong.core.application.commands.board.UploadBoardCommand;
import com.anseong.core.application.commands.board.services.BoardCommandRepositoryService;
import com.anseong.core.application.commands.board.services.BoardCommandService;
import com.anseong.core.domain.client.convert.media.ConvertMediaClient;
import com.anseong.core.domain.client.convert.media.request.ConvertMediaRequest;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;
import com.anseong.core.domain.client.notificationn.NotificationClient;
import com.anseong.core.domain.client.notificationn.NotificationTopic;
import com.anseong.core.domain.client.notificationn.request.NotificationToTokensRequest;
import com.anseong.core.domain.client.notificationn.request.NotificationToTopicRequest;
import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.repository.UserCache;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class BoardCommandServiceImpl implements BoardCommandService {

     private final UserCache userCache;
     private final ConvertMediaClient convertMediaClient;
     private final NotificationClient notificationClient;
     private final BoardCommandRepositoryService boardCommandRepositoryService;

     public BoardCommandServiceImpl(
          UserCache userCache,
          ConvertMediaClient convertMediaClient,
          NotificationClient notificationClient,
          BoardCommandRepositoryService boardCommandRepositoryService
     ) {
          this.userCache = userCache;
          this.convertMediaClient = convertMediaClient;
          this.notificationClient = notificationClient;
          this.boardCommandRepositoryService = boardCommandRepositoryService;
     }

	@Override
	public void createBoard(UploadBoardCommand command, UserId userId) {
          Assert.notNull(command, "게시판 서비스 : 파라미터 `command`가 비어있습니다.");
          Assert.notNull(userId, "게시판 서비스 : 파라미터 `userId`가 비어있습니다.");

          List<ConvertMediaRequest> convertMediaRequests = CreateBoardManagement.createBoardImageRequests(command);
          List<ConvertMediaResponse> convertMediaResponses = convertMediaClient.process(convertMediaRequests);

          String contents = CreateBoardManagement.replaceMediaOriginalSrcToConvertedSrcFromContents(command.imageSrc(), convertMediaResponses, command.contents());
          String thumbnailImage = CreateBoardManagement.replaceThumbnail(command.selectedThumbnail(), convertMediaResponses, contents);

          BoardId savedBoardId = boardCommandRepositoryService.createBoard(command, userId, contents, thumbnailImage);

          Set<String> referStoreFcmTokens = userCache.getCeoFcmTokenByIds(command.stores().stream().map(e -> e.userId()).toList());

          NotificationToTokensRequest referRequest = NotificationToTokensRequest.builder()
               .tokens(referStoreFcmTokens)
               .userId(userId)
               .thumbnail(thumbnailImage)
               .redirect("/boards/" + savedBoardId.value() + "?redirect=true")
               .build();

          NotificationToTopicRequest topicRequest = NotificationToTopicRequest.builder()
               .topic(command.category().equals("사장님") ? NotificationTopic.CEO : NotificationTopic.BOARD)
               .userId(userId)
               .description(command.title())
               .thumbnail(thumbnailImage)
               .redirect("/boards/" + savedBoardId.value() + "?redirect=true")
               .build();

          notificationClient.sendToTokens(referRequest);
          notificationClient.sendToTopic(topicRequest);
	}

     @Override
     public void updateBoard(UploadBoardCommand command, UserId userId) {
          Assert.notNull(command, "게시판 서비스 : 파라미터 `command`가 비어있습니다.");
          Assert.notNull(userId, "게시판 서비스 : 파라미터 `userId`가 비어있습니다.");

          List<ConvertMediaRequest> convertMediaRequests = CreateBoardManagement.createBoardImageRequests(command);
          List<ConvertMediaResponse> convertMediaResponses = convertMediaClient.process(convertMediaRequests);

          String contents = CreateBoardManagement.replaceMediaOriginalSrcToConvertedSrcFromContents(command.imageSrc(), convertMediaResponses, command.contents());
          String thumbnailImage = CreateBoardManagement.replaceThumbnail(command.selectedThumbnail(), convertMediaResponses, contents);

          boardCommandRepositoryService.updateBoard(command, userId, contents, thumbnailImage);
     }

     @Override
     public void deleteBoard(BoardId boardId) {
          boardCommandRepositoryService.deleteBoard(boardId);
     }

     @Override
     public void addBoardStore(AddBoardStoreCommand command) {
          boardCommandRepositoryService.addBoardStore(command);
     }

     @Override
     public void deleteBoardStore(BoardId boardId, UserId storeId) {
          boardCommandRepositoryService.deleteBoardStore(boardId, storeId);
     }
}
