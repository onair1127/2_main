package com.anseong.core.web.apis;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import com.anseong.core.application.commands.user.RegistrationCommand;
import com.anseong.core.application.commands.user.UserType;
import com.anseong.core.application.commands.user.services.UserCommandService;
import com.anseong.core.application.queries.user.services.UserQueryService;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.domain.model.user.exception.NicknameExistsException;
import com.anseong.core.domain.model.user.exception.PhoneExistsException;
import com.anseong.core.domain.model.user.exception.RegistrationException;
import com.anseong.core.domain.model.user.exception.UsernameExistsException;
import com.anseong.core.web.apis.advice.UserUpdated;
import com.anseong.core.web.payloads.RegistrationPayload;
import com.anseong.core.web.payloads.ValidateUserPayload;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.result.Result;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/registration")
class RegistrationApiController {

     private final UserQueryService userQueryService;
     private final UserCommandService userCommandService;

     RegistrationApiController(UserQueryService userQueryService, UserCommandService userCommandService) {
          this.userQueryService = userQueryService;
          this.userCommandService = userCommandService;
     }

     @PostMapping
     ResponseEntity<ApiResult> registUser(
               @Valid @RequestBody RegistrationPayload payload
     ) {
          final RegistrationCommand command = payload.toCommand();

          try {
               userCommandService.register(command, UserType.USER);
               return Result.created();
          } catch (RegistrationException e) {
               String errorMessage = "회원가입에 실패. ";
               if (e instanceof PhoneExistsException) {
                    errorMessage = "이미 가입된 회원입니다.";
               } else {
                    errorMessage = "회원가입에 실패하였습니다.\n잠시후 다시 시도해주세요.";
               }

               return Result.failure(errorMessage);
          }
     }

     @PostMapping("/CEO")
     @UserUpdated
     ResponseEntity<ApiResult> registerCEO(
               @Valid @RequestBody RegistrationPayload payload,
               HttpServletResponse response
     ) {
          try {
               SimpleUser user = userCommandService.registerCEO(payload.toCommand());
               Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
               SecurityContextHolder.getContext().setAuthentication(auth);

               return Result.created();

          } catch (RegistrationException e) {
               String errorMessage = "회원가입에 실패. ";
               if (e instanceof PhoneExistsException) {
                    errorMessage = "이미 가입된 회원입니다.";
               } else {
                    errorMessage = "회원가입에 실패하였습니다.\n잠시후 다시 시도해주세요.";
               }

               return Result.failure(errorMessage);
          }
     }

     @PostMapping("/validate")
     public ResponseEntity<ApiResult> validateUser(
               @Valid @RequestBody ValidateUserPayload payload,
               @RequestParam("nicknameCreated") boolean nicknameCreated
     ) {
          try {
               userQueryService.validateUser(payload.toCommand(), nicknameCreated);
               return Result.ok();
          } catch (RegistrationException e) {
               String errorMessage = "회원가입에 실패. ";
               if (e instanceof UsernameExistsException) {
                    errorMessage = "아이디가 이미 존재합니다.";
               } else if (e instanceof NicknameExistsException) {
                    errorMessage = "닉네임이 이미 존재합니다.";
               }

               return Result.failure(errorMessage);
          }
     }
}
