package com.anseong.global.config;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

import com.anseong.core.web.result.Result;
import com.anseong.global.utils.JsonUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.filter.GenericFilterBean;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestRejectedExceptionFilter extends GenericFilterBean {

     private static final Logger log = LoggerFactory.getLogger(RequestRejectedExceptionFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
              throws IOException, ServletException {
     
          try {
               chain.doFilter(request, response);

          } catch (RequestRejectedException e) {
               log.warn("요청 거부 : 블랙리스트 문자가 들어옴. [{}]", e.getMessage());
               
               HttpServletResponse res = (HttpServletResponse) response;
               res.setStatus(HttpStatus.BAD_REQUEST.value());
               res.setCharacterEncoding("utf-8");

               JsonUtils.write(res.getWriter(), Result.failure("특수문자가 포함되어있습니다."));
          }
	}
}
