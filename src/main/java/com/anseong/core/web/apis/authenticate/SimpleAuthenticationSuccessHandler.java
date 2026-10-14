package com.anseong.core.web.apis.authenticate;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.domain.model.user.repository.UserCache;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.whatthefuckisthis.AES256;
import com.anseong.core.web.whatthefuckisthis.JwtUtils;
import com.anseong.global.utils.JsonUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

     private final Encryptor cipherEncryptor = Encryptors.AES256();
     private final TokenIssuer tokenIssuer = TokenIssuers.JWT();
     private UserCache userCache;

     public SimpleAuthenticationSuccessHandler(
          UserCache userCache
     ) {
          this.userCache = userCache;
     }

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
              Authentication authentication) throws IOException, ServletException {

          try {
               SimpleUser s = (SimpleUser) authentication.getPrincipal();
               TokenIssueResult issuedToken = tokenIssuer.issue(s);

               final String accessToken = cipherEncryptor.encrypt(issuedToken.accessToken());
               final String refreshToken = cipherEncryptor.encrypt(issuedToken.refreshToken());

               Map<String, List<Integer>> userData = userCache.getUserData(s.getUserId());
               ResponseCookie cookie = ResponseCookie.fromClientResponse("_rt", refreshToken)
                    .domain(".anseongapp.com")
                    .httpOnly(true)
                    .sameSite("Strict")
                    .secure(true)
                    .path("/api")
                    .maxAge(Duration.ofDays(99999))
                    .build();

               response.setHeader("Authorization", accessToken);
               response.setStatus(HttpStatus.OK.value());
               response.setCharacterEncoding("utf-8");
               response.addHeader("Set-Cookie", cookie.toString());
               JsonUtils.write(response.getWriter(), ApiResult.blank()
                    .add("message", "인증 완료")
                    .add("temporary", s.isTemporary())
                    .add("nickname", s.getNickname())
                    .add("ceo", s.getCeoId().isValid())
                    // .add("userId", s.getUserId().value())
                    .add("notRegisteredCard", s.isNotRegisteredCard())
                    .add("storeCreated", s.isStoreCreated())
                    .add("favoriteList", userData.get("favoriteList"))
                    .add("boardLikeList", userData.get("boardLikeList"))
                    .add("imageSort", s.getImageSort())
                    .add("profile", s.getProfile())
                    .add("logo", s.getLogo()));

          } catch (Exception e) {
               e.printStackTrace();
               response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
               response.setCharacterEncoding("utf-8");
               JsonUtils.write(response.getWriter(), ApiResult.message("로그인에 실패하였습니다. 관리자에게 문의 바랍니다."));
          }
     }
}
