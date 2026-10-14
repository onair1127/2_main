package com.anseong.core.web.apis;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.anseong.core.application.commands.event.services.EventCommandService;
import com.anseong.core.application.commands.report.services.ReportCommandService;
import com.anseong.core.application.commands.user.ResetMyPasswordCommand;
import com.anseong.core.application.commands.user.UpdateMyPasswordCommand;
import com.anseong.core.application.commands.user.UpdateMyProfileCommand;
import com.anseong.core.application.commands.user.services.UserCommandService;
import com.anseong.core.application.queries.user.services.UserQueryService;
import com.anseong.core.domain.client.certification.exception.CertificationFailedException;
import com.anseong.core.domain.client.notificationn.NotificationTopic;
import com.anseong.core.domain.exception.GlobalAnseongappException;
import com.anseong.core.domain.model.image.exception.ImageSaveFailedException;
import com.anseong.core.domain.model.user.CeoId;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.exception.EventAlreadyRegisteredException;
import com.anseong.core.domain.model.user.exception.NicknameExistsException;
import com.anseong.core.domain.model.user.repository.UserCache;
import com.anseong.core.web.apis.advice.UserUpdated;
import com.anseong.core.web.apis.authenticate.CurrentUser;
import com.anseong.core.web.payloads.AddUserFcmTokenPayload;
import com.anseong.core.web.payloads.RegisterReviewEventPayload;
import com.anseong.core.web.payloads.ReportServicePayload;
import com.anseong.core.web.payloads.ResetMyPasswordPayload;
import com.anseong.core.web.payloads.UpdateMyPasswordPayload;
import com.anseong.core.web.payloads.UpdateMyProfilePayload;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.result.Result;
import com.anseong.core.web.result.UserResult;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/me")
class UserApiController {

     private UserCommandService userCommandService;
     private UserQueryService userQueryService;
     private ReportCommandService reportCommandService;
     private EventCommandService eventCommandService;
     private UserCache userCache;

     @GetMapping
     ResponseEntity<ApiResult> getMyData(
               @CurrentUser SimpleUser user,
               @Nullable @RequestParam("OS") String OS,
               @Nullable @RequestParam("token") String token,
               HttpServletResponse response
     ) {
          if (user == null) {
               return Result.unauthorized();
          }

          if (LocalDate.now().getYear() - user.getBirth() >= 19 && user.getUserId().value() != 0 && !user.isAdult()) {
               userCommandService.setToAdult(user.getUserId());

               user.setAdult(true);
               setAuthentication(user);
          }

          if (!user.getUserId().isValid()
               || (user.getCeoId().isValid() &&
                    (!user.isStoreCreated()))) {

               return Result.unauthorized();

          } else {
               List<Integer> favoriteList = null;
               List<Integer> boardLikeList = null;

               if (user.getUserId().value() != 0) {
                    Map<String, List<Integer>> userData = userCache.getUserData(user.getUserId());

                    favoriteList = userData.get("favoriteList");
                    boardLikeList = userData.get("boardLikeList");
               }

               return UserResult.build(
                    user,
                    token != null ? userQueryService.getSubscribeBoardStatus(user.getUserId(), OS, token) : false,
                    favoriteList,
                    boardLikeList);
          }
     }

     @GetMapping("/guest")
     @UserUpdated
     ResponseEntity<ApiResult> getGuestData(
          HttpServletResponse response
     ) throws Exception {
          final SimpleUser user = SimpleUser.create(new UserId(0), new CeoId(null), "__GUEST__", "__GUEST__", 9999, 3, false, false, false, false, null, null);
          setAuthentication(user);

          return Result.created();
     }

     @PostMapping("/add/fcm")
     ResponseEntity<ApiResult> addUserFCMToken(
               @Valid @RequestBody AddUserFcmTokenPayload payload,
               @CurrentUser SimpleUser user
     ) {

          /**
           * ... 이거 이름 좀 찝찝하긴 한데
           * 스토어에 앱 배포할 때 설계 잘못하고 OTA 업데이트 안된 버전도 정상 작동 해야돼서
           * 이름 절대 바꾸면 안됨 ㅠㅠ
           *
           * 나중에 새 버전 배포하면 그때 바꾸자
           */
          boolean hasKey = userCommandService.addFCMToken(user.getUserId(), payload.toCommand(), user.getCeoId().isValid(), user.isAdult());
          return Result.ok(ApiResult.blank().add("hasKey", hasKey));
     }

     @PostMapping("/logout")
     @UserUpdated
     ResponseEntity<ApiResult> logout(
               @CurrentUser SimpleUser user,
               HttpServletResponse response
     ) throws Exception {
          final SimpleUser guest = SimpleUser.create(new UserId(0), new CeoId(null), "__GUEST__", "__GUEST__", 9999, 3, false, false, false, false, null, null);
          setAuthentication(guest);

          return Result.created();
     }

     @PutMapping("/signout")
     @UserUpdated
     ResponseEntity<ApiResult> signout(
               @CurrentUser SimpleUser user,
               HttpServletResponse response
     ) throws Exception {

          userCommandService.signout(user.getUserId(), user.getCeoId().isValid());

          final SimpleUser guest = SimpleUser.create(new UserId(0), new CeoId(null), "__GUEST__", "__GUEST__", 9999, 3, false, false, false, false, null, null);
          setAuthentication(guest);

          return Result.created();
     }

     @GetMapping("/find/username")
     ResponseEntity<ApiResult> findUsername(
               @NotNull @NotEmpty @RequestParam("impUid") String impUid
     ) throws CertificationFailedException {
          try {
               userQueryService.getUsernameByPhone(impUid);
               return Result.ok(userQueryService.getUsernameByPhone(impUid));

          } catch (Exception e) {
               if (e instanceof CertificationFailedException) {
                    return Result.failure(e.getMessage());
               } else {
                    return Result.failure("아이디 찾기에 실패하였습니다.\n잠시후 다시 시도해주세요.");
               }
          }
     }

     @PutMapping("/reset/password")
     ResponseEntity<ApiResult> resetMyPassword(
               @Valid @RequestBody ResetMyPasswordPayload payload
     ) throws CertificationFailedException {
          final ResetMyPasswordCommand command = payload.toCommand();

          try {
               userCommandService.resetMyPassword(command);
               return Result.noContent();
          } catch (Exception e) {
               if (e instanceof CertificationFailedException) {
                    return Result.failure(e.getMessage());
               } else {
                    return Result.failure("비밀번호 찾기에 실패하였습니다.\n잠시후 다시 시도해 주세요.");
               }
          }
     }

     @PutMapping("/update/password")
     ResponseEntity<ApiResult> updateMyPassword(
               @Valid @RequestBody UpdateMyPasswordPayload payload
     ) throws CertificationFailedException {

          final UpdateMyPasswordCommand command = payload.toCommand();

          try {
               userCommandService.updateMyPassword(command);
               return Result.noContent();
          } catch (Exception e) {
               if (e instanceof CertificationFailedException) {
                    return Result.failure("비밀번호 재설정에 실패하였습니다.\n잠시후 다시 시도해 주세요.");
               } else {
                    throw new GlobalAnseongappException("비밀번호 재설정 로직 실패 : " + e.getMessage(), e.getCause());
               }
          }
     }

     @PostMapping("/subscribe/board")
     ResponseEntity<ApiResult> subscribeBoard(
               @Valid @RequestBody AddUserFcmTokenPayload payload,
               @CurrentUser SimpleUser user
     ) {
          userCommandService.subscribeTopic(user.getUserId(), payload.toCommand(), NotificationTopic.BOARD, user.getCeoId().isValid(), user.isAdult());
          return Result.created();
     }

     @DeleteMapping("/unsubscribe/board")
     ResponseEntity<ApiResult> unsubscribeBoard(
               @CurrentUser SimpleUser user,
               @NotNull @NotEmpty @RequestParam("OS") String OS,
               @NotNull @NotEmpty @RequestParam("token") String token
     ) {
          userCommandService.unsubscribeTopic(user.getUserId(), token, OS, NotificationTopic.BOARD, user.getCeoId().isValid(), user.isAdult());
          return Result.noContent();
     }

     @PostMapping("/report")
     ResponseEntity<ApiResult> reportService(
               @CurrentUser SimpleUser user,
               @Valid @RequestBody ReportServicePayload payload
     ) throws ImageSaveFailedException {
          reportCommandService.reportService(user.getUserId(), payload.toCommand());
          return Result.created();
     }

     @PostMapping("/update/profile")
     @UserUpdated
     ResponseEntity<ApiResult> updateMyProfile(
               @CurrentUser SimpleUser user,
               @RequestBody UpdateMyProfilePayload payload,
               HttpServletResponse response
     ) {
          final UpdateMyProfileCommand command = payload.toCommand();

          try {
               userCommandService.updateMyProfile(user.getUserId(), command);

               user.setNickname(command.nickname());
               user.setProfile(command.profile());
               user.setLogo(command.logo());

               setAuthentication(user);
               return Result.created();

          } catch (NicknameExistsException e) {
               return Result.failure("이미 존재하는 닉네임.");

          }
     }


     @PostMapping("/register/event")
     ResponseEntity<ApiResult> registerEvent(
               @CurrentUser SimpleUser user,
               @Valid @RequestBody RegisterReviewEventPayload payload
     ) {
          try {
               eventCommandService.registerReviewEvent(user.getUserId(), payload.toCommand());
               return Result.created();
          } catch (Exception e) {
               if (e instanceof EventAlreadyRegisteredException) {
                    return Result.forbidden();
               } else {
                    throw new GlobalAnseongappException("이벤트 신청 실패 : " + e.getMessage(), e.getCause());
               }
          }
     }

     @GetMapping("/validate/event")
     ResponseEntity<ApiResult> validateEvent(
          @CurrentUser SimpleUser user
     ) {
          boolean exists = eventCommandService.validate(user.getUserId());

          if (exists)
               return Result.forbidden();
          else
               return Result.ok();
     }

     @PutMapping("/set/imageSort/{imageSort}")
     @UserUpdated
     ResponseEntity<ApiResult> setUserImageSortValue(
          @PathVariable("imageSort") int imageSort,
          @CurrentUser SimpleUser user,
          HttpServletResponse response
     ) throws Exception {
          user.setImageSort(imageSort);

          if (user.getUserId().value() != 0)
               userCache.setImageSort(user.getUserId(), imageSort);

          setAuthentication(user);
          return Result.created();
     }

     private void setAuthentication(SimpleUser user) {
          Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(auth);
     }
}
