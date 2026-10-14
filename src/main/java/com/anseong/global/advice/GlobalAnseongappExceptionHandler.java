package com.anseong.global.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.anseong.core.domain.exception.GlobalAnseongappException;
import com.anseong.core.web.result.ApiResult;
import com.anseong.core.web.result.Result;

@ControllerAdvice
public class GlobalAnseongappExceptionHandler {

     private static final Logger log = LoggerFactory.getLogger("ERROR-GLOBAL-LOGGER");

     @ExceptionHandler(GlobalAnseongappException.class)
     ResponseEntity<ApiResult> handleApplicationException(GlobalAnseongappException e) {
          log.warn("비즈니스 로직 실패 -> [{} : {}]", e.getMessage(), e.getCause());

          return Result.failure("서버 문제로 요청 처리에 실패하였습니다. 나중에 다시 시도해 주시기 바랍니다.");
     }

     @ExceptionHandler(Exception.class)
     ResponseEntity<ApiResult> handleUnexpectedException(Exception e) {
          log.error("!!! 예측 못한 에러 발생 -> [{} : {}]", e.getMessage(), e.getCause());

          return Result.failure("서버 문제로 요청 처리에 실패하였습니다. 나중에 다시 시도해 주시기 바랍니다.");
     }
}
