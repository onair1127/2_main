package com.anseong.core.web.apis;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.anseong.core.application.commands.image.DeleteImageCommand;
import com.anseong.core.application.commands.image.UploadImageCommand;
import com.anseong.core.application.commands.image.services.ImageCommandService;
import com.anseong.core.application.commands.sns.services.SnsCommandService;
import com.anseong.core.application.commands.store.MakeOwnStoreCommand;
import com.anseong.core.application.commands.store.UpdateMyStoreCommand;
import com.anseong.core.application.commands.store.services.StoreCommandService;
import com.anseong.core.application.commands.tag.services.TagCommandService;
import com.anseong.core.application.commands.user.services.UserCommandService;
import com.anseong.core.application.queries.image.services.ImageQueryService;
import com.anseong.core.application.queries.store.services.StoreQueryService;
import com.anseong.core.application.queries.tag.services.TagQueryService;
import com.anseong.core.application.queries.user.services.UserQueryService;
import com.anseong.core.domain.client.certification.exception.CertificationFailedException;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.exception.ImageSaveFailedException;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.store.exception.SnsSaveFailedException;
import com.anseong.core.domain.model.store.exception.StoreDuplicatedException;
import com.anseong.core.domain.model.store.exception.StoreSaveFailedException;
import com.anseong.core.domain.model.store.exception.TagSaveFailedException;
import com.anseong.core.domain.model.store.response.StoreForCertificateCardResponse;
import com.anseong.core.domain.model.store.response.StoreResponse;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.web.apis.advice.UserUpdated;
import com.anseong.core.web.apis.authenticate.CurrentUser;
import com.anseong.core.web.payloads.AddCommonTagPayload;
import com.anseong.core.web.payloads.AddPersonalTagPayload;
import com.anseong.core.web.payloads.AddSnsPayload;
import com.anseong.core.web.payloads.MakeOwnStorePayload;
import com.anseong.core.web.payloads.RegistrationPayload;
import com.anseong.core.web.payloads.UpdateImagePayload;
import com.anseong.core.web.payloads.UpdateMyStorePayload;
import com.anseong.core.web.payloads.UploadImagePayload;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.result.BindTimeResult;
import com.anseong.core.web.result.Result;
import com.anseong.core.web.result.UpdatableImageResults;
import com.anseong.core.web.result.UpdatableStoreResult;

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
@RequestMapping("/api/ceo")
class CeoApiController {

     private final StoreCommandService storeCommandService;
     private final StoreQueryService storeQueryService;
     private final ImageCommandService imageCommandService;
     private final ImageQueryService imageQueryService;
     private final UserQueryService userQueryService;
     private final UserCommandService userCommandService;
     private final TagQueryService tagQueryService;
     private final TagCommandService tagCommandService;
     private final SnsCommandService snsCommandService;

     CeoApiController(
          StoreCommandService storeCommandService
          , StoreQueryService storeQueryService
          , ImageCommandService imageCommandService
          , ImageQueryService imageQueryService
          , UserQueryService userQueryService
          , UserCommandService userCommandService
          , TagQueryService tagQueryService
          , TagCommandService tagCommandService
          , SnsCommandService snsCommandService
     ) {
          this.storeCommandService = storeCommandService;
          this.storeQueryService = storeQueryService;
          this.imageCommandService = imageCommandService;
          this.imageQueryService = imageQueryService;
          this.userQueryService = userQueryService;
          this.userCommandService = userCommandService;
          this.tagQueryService = tagQueryService;
          this.tagCommandService = tagCommandService;
          this.snsCommandService = snsCommandService;
     }

     @GetMapping("/store")
     ResponseEntity<ApiResult> getMyStore(
               @CurrentUser SimpleUser user
     ) {
          StoreResponse storeResponse = storeQueryService.getUpdatableStore(user.getUserId());
          return UpdatableStoreResult.build(storeResponse);
     }

     @GetMapping("/store/validate")
     ResponseEntity<ApiResult> validateStore(
               @NotNull @NotEmpty @RequestParam("userId") long userId,
               @NotNull @NotEmpty @RequestParam("bizNo") long bizNo,
               @CurrentUser SimpleUser user
     ) {

          try {
               storeCommandService.validateStore(new UserId(userId), bizNo);
               return Result.ok();
          } catch (StoreDuplicatedException e) {
               return Result.failure("이미 등록된 가게입니다.");
          }
     }

     @PostMapping("/store/new")
     @UserUpdated
     ResponseEntity<ApiResult> makeOwnStore(
               @Valid @RequestBody MakeOwnStorePayload payload,
               @CurrentUser SimpleUser user,
               HttpServletResponse response
     ) throws Exception {

          final MakeOwnStoreCommand command = payload.toCommand();

          storeCommandService.createStore(command, user.getUserId(), user.getNickname());
          user.setStoreCreated(true);
          user.setNickname(command.name());

          return Result.created();
     }

     @GetMapping("/bind-time")
     ResponseEntity<ApiResult> getBindTime(
               @CurrentUser SimpleUser user
     ) {
          List<LocalTime> timeList = Stream
               .iterate(LocalTime.of(0, 0), t -> t.plusMinutes(30))
               .limit(24 * 2)
               .collect(Collectors.toList());

          return BindTimeResult.build(timeList, user.getUserId());
     }

     @DeleteMapping("/image/delete/{type}/{imageId}")
     ResponseEntity<ApiResult> deleteImage(
               @NotNull @PathVariable("type") String type,
               @NotNull @PathVariable("imageId") long imageId,
               @Nullable @RequestParam("archived") boolean archived,
               @CurrentUser SimpleUser user
     ) {
          DeleteImageCommand command = new DeleteImageCommand(new ImageId(imageId), user.getUserId(), archived, type);
          imageCommandService.deleteImage(command);

          return Result.noContent();
     }

     @PutMapping("/store/update")
     @UserUpdated
     ResponseEntity<ApiResult> updateMyStore(
               @Valid @RequestBody UpdateMyStorePayload payload,
               @CurrentUser SimpleUser user,
               HttpServletResponse response
     ) throws StoreSaveFailedException, ImageSaveFailedException {
          UpdateMyStoreCommand command = payload.toCommand();
          storeCommandService.updateStore(command, user.getNickname(), user.getUserId());

          user.setStoreCreated(true);
          user.setNickname(command.name());

          return Result.noContent();
     }

     @PostMapping("/sns/add")
     ResponseEntity<ApiResult> addSns(
               @Valid @RequestBody AddSnsPayload payload,
               @CurrentUser SimpleUser user
     ) throws SnsSaveFailedException {
          snsCommandService.add(payload.toCommand(), user.getUserId());
          return Result.ok();
     }

     @DeleteMapping("/sns/delete/{type}")
     ResponseEntity<ApiResult> deleteSns(
               @PathVariable("type") String type,
               @CurrentUser SimpleUser user
     ) {
          snsCommandService.delete(type, user.getUserId());
          return Result.created();
     }

     @GetMapping("/images/{lastImageId}")
     ResponseEntity<ApiResult> getMyImages(
               @NotNull @PathVariable("lastImageId") long lastImageId,
               @CurrentUser SimpleUser user
     ) {
          List<ImageResponse> imageResponses = imageQueryService.getUpdatableImages(user.getUserId(), new ImageId(lastImageId), true);
          return UpdatableImageResults.build(imageResponses);
     }

     @GetMapping("/tags")
     ResponseEntity<ApiResult> getMyCommonTags(
          @CurrentUser SimpleUser user
     ) {
          return Result.ok(
               ApiResult.blank()
                    .add("tags", tagQueryService.getCommonTagNamesByUserId(user.getUserId())
               ));
     }


     @PutMapping("/image/update/{type}")
     ResponseEntity<ApiResult> submitUpdate(
               @Valid @RequestBody UpdateImagePayload payload,
               @NotNull @PathVariable("type") String type,
               @CurrentUser SimpleUser user
     ) {
          imageCommandService.updateImage(payload.toCommand(), user.getUserId(), type);
          return Result.ok();
     }

     @PostMapping("/tag/add")
     ResponseEntity<ApiResult> addCommonTag(
               @Valid @RequestBody AddCommonTagPayload payload,
               @CurrentUser SimpleUser user
     ) throws TagSaveFailedException {
          tagCommandService.addCommonTag(payload.toCommand(), user.getUserId());
          return Result.created();
     }

     @PostMapping("/tag/personal/add")
     ResponseEntity<ApiResult> addPersonalTag(
               @Valid @RequestBody AddPersonalTagPayload payload,
               @CurrentUser SimpleUser user
     ) throws TagSaveFailedException {
          tagCommandService.addPersonalTag(payload.toCommand(), user.getUserId());
          return Result.created();
     }

     @DeleteMapping("/tag/delete")
     ResponseEntity<ApiResult> deleteCommonTag(
               @NotEmpty @RequestParam("tag") String tag,
               @CurrentUser SimpleUser user
     ) throws TagSaveFailedException {
          tagCommandService.deleteCommonTags(user.getUserId(), tag);
          return Result.ok();
     }

     @DeleteMapping("/tag/personal/delete")
     ResponseEntity<ApiResult> deletePersonalTag(
               @RequestParam("image") long image,
               @NotEmpty @RequestParam("tag") String tag,
               @CurrentUser SimpleUser user
     ) {
          tagCommandService.deletePersonalTag(new ImageId(image), tag);
          return Result.ok();
     }

     @PostMapping("/image/upload")
     ResponseEntity<ApiResult> uploadImage(
               @RequestBody UploadImagePayload payload,
               @CurrentUser SimpleUser user
     ) throws ImageSaveFailedException {

          UploadImageCommand command = payload.toCommand();
          imageCommandService.uploadImage(command, user.getUserId(), user.getNickname(), command.type());
          return Result.created();
     }

     @GetMapping("/get/merchant")
     ResponseEntity<ApiResult> getMerchantUID(
               @CurrentUser SimpleUser user
     ) {
          String merchantUid = userQueryService.getMerchantUidById(user.getUserId());
          return Result.ok(ApiResult.blank().add("merchantUid", merchantUid));
     }

     @PutMapping("/certificate/phone")
     ResponseEntity<ApiResult> certificatePhone(
               @Valid @RequestBody RegistrationPayload payload,
               @CurrentUser SimpleUser user
     ) throws CertificationFailedException {

          userCommandService.registerNewCeo(user.getUserId(), payload.toCommand());
          return Result.created();
     }

     @GetMapping("/certificate/finish")
     @UserUpdated
     ResponseEntity<ApiResult> certificateFinish(
               @CurrentUser SimpleUser user,
               HttpServletResponse response
     ) throws Exception {
          user.setTemporary(false);
          user.setNotRegisteredCard(false);

          return Result.created();
     }

     @GetMapping("/certificate/get/store")
     private ResponseEntity<ApiResult> getStoreInfoForCertificateCard(
               @CurrentUser SimpleUser user
     ) {
          StoreForCertificateCardResponse response = storeQueryService.getStoreForCertificateCard(user.getUserId());

          return Result.ok(ApiResult.blank()
               .add("userId", response.userId().value())
               .add("name", response.storeName())
               .add("phone", response.phone())
               .add("address", response.address()));
     }
}
