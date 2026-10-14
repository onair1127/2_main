package com.anseong.core.web.apis.authenticate;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.web.result.ApiResult;
import com.anseong.global.utils.JsonUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class SimpleLogoutSuccessHandler implements LogoutSuccessHandler {

     private static final Logger log = LoggerFactory.getLogger(SimpleLogoutSuccessHandler.class);

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
              throws IOException, ServletException {

          try {
               SimpleUser s = (SimpleUser) authentication.getPrincipal();
               log.info("로그아웃 : [{}]", s.getUsername());

          } catch (Exception e) {
               log.error("로그아웃 유저 정보 호출 실패 : ", e.getMessage());
          }

          response.setStatus(HttpStatus.OK.value());
          response.setCharacterEncoding("utf-8");
          JsonUtils.write(response.getWriter(), ApiResult.message("로그아웃에 성공하였습니다."));
	}
}
