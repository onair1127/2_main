package com.anseong.core.web.apis.advice;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.UriComponentsBuilder;

import com.anseong.core.application.commands.user.services.UserCommandService;
import com.anseong.core.application.queries.user.services.UserQueryService;
import com.anseong.core.domain.exception.GlobalAnseongappException;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.domain.model.user.repository.UserCache;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.result.UserResult;

@RestControllerAdvice
class ApiResponseAdvice implements ResponseBodyAdvice<Object>  {

     private final Encryptor cipherEncryptor = Encryptors.AES256();
     private final TokenIssuer tokenIssuer = TokenIssuers.JWT();

     private UserCommandService userCommandService;
     private UserQueryService userQueryService;
     private UserCache userCache;

     ApiResponseAdvice(
          UserCommandService userCommandService,
          UserCache userCache
     ) {
          this.userCommandService = userCommandService;
          this.userCache = userCache;
     }

     @Override
     @SuppressWarnings("null")
     public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
          return true;
     }

     @Override
     @Nullable
     @SuppressWarnings("null")
     public Object beforeBodyWrite(
          @Nullable Object body,
          MethodParameter returnType,
          MediaType selectedContentType,
          Class<? extends HttpMessageConverter<?>> selectedConverterType,
          ServerHttpRequest request,
          ServerHttpResponse response
     ) {
          if (body == null)
               return body;

          MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(request.getURI())
                    .build()
                    .getQueryParams();

          boolean withUserData = Boolean.parseBoolean(queryParams.getFirst("withUserData"));
          ApiResult resultBody = (ApiResult) body;

          if (withUserData) {
               SimpleUser user = (SimpleUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
               String OS = queryParams.getFirst("OS");
               String token = queryParams.getFirst("token");

               try {
                    resultBody.add("userData", getMyData(user, OS, token, response));
               } catch (Exception e) {
                    throw new GlobalAnseongappException("User 데이터 생성 중 문제 발생 : " + e.getMessage(), e.getCause());
               }
          }

          return body;
     }

     private Object getMyData(
               SimpleUser user,
               String OS,
               String token,
               ServerHttpResponse response
     ) throws Exception {
          if (LocalDate.now().getYear() - user.getBirth() >= 19 && user.getUserId().value() != 0 && !user.isAdult()) {
               user.setAdult(true);
               userCommandService.setToAdult(user.getUserId());
               updateUser(user, response);
          }

          if (!user.getUserId().isValid()
               || (user.getCeoId().isValid() &&
                    (!user.isStoreCreated()))) {

               return null;

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

     private void updateUser(SimpleUser user, ServerHttpResponse response) throws Exception {
          SimpleUser s = (SimpleUser) authentication.getPrincipal();
          TokenIssueResult issuedToken = tokenIssuer.issue(s);

          final String accessToken = cipherEncryptor.encrypt(issuedToken.accessToken());
          final String refreshToken = cipherEncryptor.encrypt(issuedToken.refreshToken());

          ResponseCookie cookie = ResponseCookie.fromClientResponse("_rt", refreshToken)
               .domain(".anseongapp.com")
               .httpOnly(true)
               .sameSite("Strict")
               .secure(true)
               .path("/api")
               .maxAge(Duration.ofDays(99999))
               .build();

          response.getHeaders().set("Authorization", accessToken);
          response.getHeaders().set("Set-Cookie", cookie.toString());
     }
}
