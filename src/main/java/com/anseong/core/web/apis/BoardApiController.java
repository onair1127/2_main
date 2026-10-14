package com.anseong.core.web.apis;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import com.anseong.core.application.queries.board.services.BoardQueryService;
import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board.repository.BoardCache;
import com.anseong.core.domain.model.board.response.BoardResponse;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.web.apis.authenticate.CurrentUser;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.result.BoardResults;
import com.anseong.core.web.result.Result;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class BoardApiController {

     private final BoardQueryService boardQueryService;
     private final BoardCache boardCache;

     BoardApiController(
          BoardQueryService boardQueryService,
          BoardCache boardCache
     ) {
          this.boardQueryService = boardQueryService;
          this.boardCache = boardCache;
     }

     @GetMapping("/boards/{page}")
     ResponseEntity<ApiResult> getBoards(
               @PathVariable("page") long lastBoardId,
               @CurrentUser SimpleUser user
     ) throws Exception {
          List<BoardResponse> boardResponses = boardQueryService.getBoards(new BoardId(lastBoardId), user.getCeoId().isValid(), user.isAdult());
          return BoardResults.build(boardResponses);
     }

     @PostMapping("/add/view/board/{boardId}")
     ResponseEntity<ApiResult> addView(
               @NotNull @PathVariable("boardId") long boardId,
               @CurrentUser SimpleUser user
     ) {
          boardCache.addBoardView(new BoardId(boardId));
          return Result.created();
     }

     @PostMapping("/add/like/board/{boardId}")
     ResponseEntity<ApiResult> addLike(
               @NotNull @PathVariable("boardId") long boardId,
               @CurrentUser SimpleUser user
     ) {
          boardCache.addBoardLike(new BoardId(boardId), user.getUserId());
          return Result.created();
     }

     @DeleteMapping("/remove/like/board/{boardId}")
     ResponseEntity<ApiResult> removeLike(
               @NotNull @PathVariable("boardId") long boardId,
               @CurrentUser SimpleUser user
     ) {
          boardCache.removeBoardLike(new BoardId(boardId), user.getUserId());
          return Result.created();
     }
}
