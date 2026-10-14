package com.anseong.core.web.apis.advice;

import java.time.Duration;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.anseong.core.domain.exception.GlobalAnseongappException;
import com.anseong.core.domain.model.user.SimpleUser;

@RestControllerAdvice
class UpdateUserAdvice implements ResponseBodyAdvice<Object> {


     @Override
     @SuppressWarnings("null")
     public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
          return returnType.hasMethodAnnotation(UserUpdated.class);
     }

     @Override
     @SuppressWarnings("null")
     public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType,
               Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
               ServerHttpResponse response) {

          SimpleUser user = (SimpleUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          try {
               updateUser(user, response);
          } catch (Exception e) {
               throw new GlobalAnseongappException("User 데이터 업데이트 중 문제 발생 : " + e.getMessage(), e.getCause());
          }

          return body;
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
