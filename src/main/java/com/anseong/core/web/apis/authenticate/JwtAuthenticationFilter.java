package com.anseong.core.web.apis.authenticate;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.anseong.core.domain.model.user.CeoId;
import com.anseong.core.domain.model.user.SimpleUser;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.common.security.cipher.Decryptor;
import com.anseong.common.security.cipher.Encryptor;
import com.anseong.common.security.cipher.provider.Decryptors;
import com.anseong.common.security.cipher.provider.Encryptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

public class JwtAuthenticationFilter extends GenericFilterBean {

     private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
     private static final String[] ONLY_CEO = {
          "/api/ceo/store/validate", "/api/ceo/store/new", "/api/ceo/profile/add", "/api/ceo/bind-time", "/api/me/add/fcm", "/api/ceo/certificate/phone", "/api/ceo/certificate/card", "/api/me/get/userId"
     };

     private final TokenVerifier tokenResolver = TokenVerifiers.JWT();
     private final String profile;

     public JwtAuthenticationFilter(String profile) {
          this.profile = profile;
     }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

          HttpServletRequest req = (HttpServletRequest) request;
          HttpServletResponse res = (HttpServletResponse) response;
          String IP = profile.equals("production") ? req.getHeader("X-Client-IP") : req.getRemoteAddr();
          String url = req.getRequestURI();
          SimpleUser user = null;

          request.setAttribute("IP", IP);
          request.setAttribute("url", url);

          if (req.getHeader("User-Agent").startsWith("ELB")) {
               chain.doFilter(request, response);
               return;

          } else if (IP.contains("52.78.100.19")
                    || IP.contains("52.78.48.223")
                    || IP.contains("52.78.5.241")
               ) {
                    SimpleUser user = SimpleUser.create(
                              new UserId(0),
                              new CeoId(null),
                              "pg",
                              "iamport",
                              99,
                              99,
                              false,
                              false,
                              false,
                              false,
                              null,
                              null);

                    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    chain.doFilter(request, response);
                    return;

          } else if (IP == null) {
               log.warn("IP 미존재 [{} :: {}]", req.getHeader("User-Agent"), url);

               chain.doFilter(request, response);
               return;
          }

          // log.info("요청 IP : {}, URL : {}", IP, url);

          try {
               user = tokenResolver.resolveClaims(req.getHeader("X-Authorization"), SimpleUser.class).claims();

          } catch (ExpiredJwtException e) {
               log.warn("Access Token 만료로 인해 재발급");
               res = JwtUtils.reissue(res, req);
               if (res != null) {
                    try {
                         claims = JwtUtils.getClaims(AES256.getInstance().decrypt(res.getHeader("Authorization")));
                    } catch (Exception e2) {
                         log.warn("재발급 후 토큰 복호화에서 문제");
                    }
               }
          } catch (IllegalArgumentException e) {
               log.warn("토큰 비어있음.");
               chain.doFilter(request, response);
               return;

          } catch (MalformedJwtException e) {
               log.warn("유효하지 않는 토큰 형식");
               chain.doFilter(request, response);
               return;

          } catch (Exception e) {
               log.warn("존재하지 않는 사용자. : {}", url);
               res.setHeader("Issue", "User Not Found");
               chain.doFilter(req, res);
               return;
          }

          if (claims != null) {
               // 어드민 API에 일반 유저가 접근할 경우
               if (url.contains("/api/admin/") && !((String) claims.get("username")).equals("onair1127")) {
                    log.warn("[{} : {}] !!! 어드민이 아닌 일반 유저가 접근함 !!", claims.get("userId"), claims.get("username"));
                    log.warn("요청 유저 IP : {}, URL : {}", IP, url); /* Proxy 전용 */
                    chain.doFilter(request, response);
                    return;
               }

               UUID ceoId = claims.get("ceoId") != null ? UUID.fromString((String) claims.get("ceoId")) : null;

               if (ceoId != null && !Boolean.parseBoolean(claims.get("storeCreated").toString())) {
                    if (!Arrays.asList(ONLY_CEO).contains(url) && !url.contains("/api/geo/")) {
                         chain.doFilter(request, response);
                         return;
                    }
               }

               Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
               SecurityContextHolder.getContext().setAuthentication(auth);

               request.setAttribute("userId", user.getUserId().value() == 0 ? "GUEST" : user.getUserId().value());
          }

          if (res != null)
               chain.doFilter(req, res);
          else
               chain.doFilter(req, response);
	}
}
