package com.anseong.core.web.apis.authenticate;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.anseong.core.web.result.ApiResult;
import com.anseong.global.utils.JsonUtils;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class SimpleAuthenticationFailureHandler implements AuthenticationFailureHandler {

     @Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
              AuthenticationException exception) throws IOException, ServletException {

          response.setStatus(HttpStatus.BAD_REQUEST.value());
          response.setCharacterEncoding("utf-8");
          ApiResult failure;

          if (exception instanceof BadCredentialsException) {
               failure = ApiResult.message("아이디 혹은 비밀빈호가 일치하지 않습니다.");
          } else if (exception instanceof InsufficientAuthenticationException) {
               failure = ApiResult.message("유효하지 않은 `인증 요청`입니다.");
          } else {
               failure = ApiResult.message("로그인에 실패하였습니다. 관리자에게 문의 바랍니다.");
          }

          JsonUtils.write(response.getWriter(), failure);
	}
}
