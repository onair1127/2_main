package com.anseong.core.web.apis;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.anseong.core.application.commands.board.services.BoardCommandService;
import com.anseong.core.application.commands.image.DeleteImageCommand;
import com.anseong.core.application.commands.image.UploadImageCommand;
import com.anseong.core.application.commands.image.services.ImageCommandService;
import com.anseong.core.application.commands.report.services.ReportCommandService;
import com.anseong.core.application.commands.sns.services.SnsCommandService;
import com.anseong.core.application.commands.store.MakeOwnStoreCommand;
import com.anseong.core.application.commands.store.services.StoreCommandService;
import com.anseong.core.application.commands.tag.services.TagCommandService;
import com.anseong.core.application.commands.user.CreateCeoAccountCommand;
import com.anseong.core.application.commands.user.services.UserCommandService;
import com.anseong.core.application.queries.board.services.BoardQueryService;
import com.anseong.core.application.queries.report.ReportImageResponse;
import com.anseong.core.application.queries.report.services.ReportImageQueryService;
import com.anseong.core.application.queries.tag.services.TagQueryService;
import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.board.response.BoardResponse;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.report.ReportImageId;
import com.anseong.core.domain.model.store.repository.StoreCache;
import com.anseong.core.domain.model.tag.repository.TagCache;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.exception.RegistrationException;
import com.anseong.core.web.apis.authenticate.CurrentUser;
import com.anseong.core.web.payloads.AddBoardStorePayload;
import com.anseong.core.web.payloads.AddRecommendTagPayload;
import com.anseong.core.web.payloads.AdminAddSnsPayload;
import com.anseong.core.web.payloads.CreateCeoAccountPayload;
import com.anseong.core.web.payloads.MakeOwnStorePayload;
import com.anseong.core.web.payloads.UploadBoardPayload;
import com.anseong.core.web.payloads.UploadImagePayload;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.result.BoardResults;
import com.anseong.core.web.result.Result;
import com.anseong.core.web.result.admin.ReportImageResults;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
class AdminApiController {

     private final ReportImageQueryService reportImageQueryService;
     private final ReportCommandService reportCommandService;

     private final ImageCommandService imageCommandService;

     private final TagCommandService tagCommandService;
     private final TagQueryService tagQueryService;
     private final TagCache tagCache;

     private final SnsCommandService snsCommandService;

     private final StoreCommandService storeCommandService;
     private final StoreCache storeCache;

     private final BoardCommandService boardCommandService;
     private final BoardQueryService boardQueryService;

     private final UserCommandService userCommandService;

     AdminApiController(
          ReportImageQueryService reportImageQueryService
          , ReportCommandService reportCommandService
          , ImageCommandService imageCommandService
          , TagCommandService tagCommandService
          , TagQueryService tagQueryService
          , TagCache tagCache
          , SnsCommandService snsCommandService
          , StoreCommandService storeCommandService
          , StoreCache storeCache
          , BoardCommandService boardCommandService
          , BoardQueryService boardQueryService
          , UserCommandService userCommandService
     ) {
          this.reportImageQueryService = reportImageQueryService;
          this.reportCommandService = reportCommandService;
          this.imageCommandService = imageCommandService;
          this.tagCommandService = tagCommandService;
          this.tagQueryService = tagQueryService;
          this.tagCache = tagCache;
          this.snsCommandService = snsCommandService;
          this.storeCommandService = storeCommandService;
          this.storeCache = storeCache;
          this.boardCommandService = boardCommandService;
          this.boardQueryService = boardQueryService;
          this.userCommandService = userCommandService;
     }

     @GetMapping("/validate")
     private ResponseEntity<ApiResult> validate(
               @CurrentUser SimpleUser user
     ) {
          return Result.accepted();
     }

     @GetMapping("/report-images/{lastReportImageId}")
     private ResponseEntity<ApiResult> getReportImages(
               @NotNull @PathVariable("lastReportImageId") int lastReportImageId,
               @CurrentUser SimpleUser user
     ) {
          List<ReportImageResponse> images = reportImageQueryService.getReportImages(new ReportImageId(lastReportImageId));
          return ReportImageResults.build(images);
     }

     @DeleteMapping("/delete/image/{type}/{storeId}/{imageId}")
     private ResponseEntity<ApiResult> deleteImage(
               @NotNull @PathVariable("type") String type,
               @NotNull @PathVariable("storeId") long userId,
               @NotNull @PathVariable("imageId") long imageId,
               @CurrentUser SimpleUser user
     ) {
          DeleteImageCommand command = new DeleteImageCommand(imageId, userId, false, type);

          imageCommandService.deleteImage(command);
          return Result.ok();
     }

     @DeleteMapping("/delete/tag")
     private ResponseEntity<ApiResult> deleteTag(
               @NotNull @RequestParam("imageId") long imageId,
               @NotNull @RequestParam("tag") String tag,
               @CurrentUser SimpleUser user
     ) {
          tagCommandService.deletePersonalTag(new ImageId(imageId), tag);

          return Result.ok();
     }

     @DeleteMapping("/delete/recommendTag")
     private ResponseEntity<ApiResult> deleteRecommendTag(
               @NotNull @RequestParam("tag") String tag,
               @CurrentUser SimpleUser user
     ) {
          tagCommandService.deleteRecommendTag(tag);

          return Result.ok();
     }

     @GetMapping("/get/add-sns")
     private ResponseEntity<ApiResult> getArchivedSnsIds(
               @CurrentUser SimpleUser user
     ) {
          return Result.ok(
               ApiResult.blank()
                    .add("ids", storeCache.getArchivedSnsIds()));
     }

     @GetMapping("/get/store/sns/{storeId}")
     private ResponseEntity<ApiResult> getStoreSns(
               @NotNull @PathVariable("storeId") long storeId,
               @CurrentUser SimpleUser user
     ) {
          return Result.ok(
               ApiResult.blank()
                    .add("sns", storeCache.getArchivedSns(new UserId(storeId))));
     }

     @GetMapping("/store/tags/get/{storeId}")
     ResponseEntity<ApiResult> getStoreCommonTags(
          @NotNull @PathVariable("storeId") long storeId,
          @CurrentUser SimpleUser user
     ) {
          return Result.ok(
               ApiResult.blank()
                    .add("tags", tagQueryService.getCommonTagNamesByUserId(new UserId(storeId))
               ));
     }

     @PostMapping("/add/store/sns")
     private ResponseEntity<ApiResult> addSns(
               @Valid @RequestBody AdminAddSnsPayload payload,
               @CurrentUser SimpleUser user
     ) {
          snsCommandService.addByAdmin(payload.toCommand());
          return Result.created();
     }

     @PostMapping("/add/tag")
     private ResponseEntity<ApiResult> addTag(
               @Valid @RequestBody AddRecommendTagPayload payload,
               @CurrentUser SimpleUser user
     ) {
          tagCache.addRecommendTag(payload.getTag(), payload.getDescription());
          return Result.created();
     }

     @PostMapping("/board/create")
     ResponseEntity<ApiResult> createBoard(
               @Valid @RequestBody UploadBoardPayload payload,
               @CurrentUser SimpleUser user
     ) {
          boardCommandService.createBoard(payload.toCommand(), user.getUserId());
          return Result.ok();
     }

     @DeleteMapping("/delete/board/{boardId}")
     ResponseEntity<ApiResult> deleteBoard(
               @NotNull @PathVariable("boardId") long boardId,
               @CurrentUser SimpleUser user
     ) {
          boardCommandService.deleteBoard(new BoardId(boardId));
          return Result.noContent();
     }

     @PostMapping("/add/board-store")
     private ResponseEntity<ApiResult> addBoardStore(
               @Valid @RequestBody AddBoardStorePayload payload,
               @CurrentUser SimpleUser user
     ) {
          boardCommandService.addBoardStore(payload.toCommand());
          return Result.created();
     }

     @DeleteMapping("/delete/board-store")
     ResponseEntity<ApiResult> deleteBoardStore(
               @NotNull @RequestParam("boardId") long boardId,
               @NotNull @RequestParam("storeId") long storeId,
               @CurrentUser SimpleUser user
     ) {
          boardCommandService.deleteBoardStore(new BoardId(boardId), new UserId(storeId));
          return Result.noContent();
     }

     @GetMapping("/get/board/{boardId}")
     private ResponseEntity<ApiResult> getUpdatableBoard(
               @NotNull @PathVariable("boardId") long boardId,
               @CurrentUser SimpleUser user
     ) {
          BoardResponse boardResponse = boardQueryService.getBoard(new BoardId(boardId));
          return BoardResults.build(List.of(boardResponse));
     }

     @PutMapping("/update/board")
     private ResponseEntity<ApiResult> updateBoard(
               @Valid @RequestBody UploadBoardPayload payload,
               @CurrentUser SimpleUser user
     ) {
          boardCommandService.updateBoard(payload.toCommand(), user.getUserId());
          return Result.noContent();
     }

     @PutMapping("/complete/report/{reportImageId}")
     private ResponseEntity<ApiResult> reportComplete(
               @NotNull @PathVariable("reportImageId") long reportImageId,
               @CurrentUser SimpleUser user
     ) {
          reportCommandService.completeReportImage(new ReportImageId(reportImageId));
          return Result.noContent();
     }

     @PostMapping("/create/ceo")
     private ResponseEntity<ApiResult> createCeoAccount(
               @Valid @RequestBody CreateCeoAccountPayload payload,
               @CurrentUser SimpleUser user
     ) {
          CreateCeoAccountCommand command = payload.toCommand();

          try {
               SimpleUser newCeo = userCommandService.createCeoAccount(command);
               return Result.ok(ApiResult.blank().add("userId", newCeo.getUserId().value()));

          } catch (RegistrationException e) {
               return Result.failure("휴대폰 번호가 이미 존재하는 계정입니다.");
          }
     }

     @PostMapping("/create/store")
     private ResponseEntity<ApiResult> createStoreByAdmin(
               @Valid @RequestBody MakeOwnStorePayload payload,
               @RequestParam("userId") long userId,
               @CurrentUser SimpleUser user
     ) {
          MakeOwnStoreCommand command = payload.toCommand();
          storeCommandService.createStore(command, new UserId(userId), command.name());

          return Result.created();
     }

     @PostMapping("/review/upload")
     ResponseEntity<ApiResult> uploadReview(
          @RequestBody UploadImagePayload payload,
          @CurrentUser SimpleUser user
     ) {

          UploadImageCommand command = payload.toCommand();
          imageCommandService.uploadImage(command, command.storeId(), "review", "review");
          return Result.ok();
     }
}
