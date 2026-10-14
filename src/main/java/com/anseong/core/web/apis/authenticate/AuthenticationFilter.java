package com.anseong.core.web.apis.authenticate;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.anseong.global.utils.JsonUtils;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
     private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

     public AuthenticationFilter() {
          super("/api/authentication");
     }

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
              throws AuthenticationException, IOException, ServletException {

          log.debug("인증 프로세스 시작(Json 타입을 객체로 매핑, 인증 후 토큰 부여)");
          String requestBody = IOUtils.toString(request.getReader());
          LoginRequest loginRequest = JsonUtils.toObject(requestBody, LoginRequest.class);

          if (loginRequest == null || loginRequest.isInvalid()) {
               throw new InsufficientAuthenticationException("유효하지 않은 인증 요청입니다.");
          }

          UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.username,
                    loginRequest.password);

          try {
               log.info("로그인 시도");
               return this.getAuthenticationManager().authenticate(token);
          } catch (AuthenticationException e) {
               log.warn("로그인 실패 : {}", e.getMessage());
               this.getFailureHandler().onAuthenticationFailure(request, response, e);
               return null;
          }
     }

     static class LoginRequest {
          private String username;
          private String password;

          public String getUsername() {
               return this.username;
          }

          public void setUsername(String username) {
               this.username = username;
          }

          public String getPassword() {
               return this.password;
          }

          public void setPassword(String password) {
               this.password = password;
          }

          public boolean isInvalid() {
               return StringUtils.isBlank(username) || StringUtils.isBlank(password);
          }
     }
}
