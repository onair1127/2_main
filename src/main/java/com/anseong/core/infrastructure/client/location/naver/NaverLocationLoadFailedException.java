package com.anseong.core.infrastructure.client.location.naver;

import org.springframework.http.HttpStatusCode;

import com.anseong.core.domain.client.location.exception.LocationLoadFailedException;

class NaverLocationLoadFailedException extends LocationLoadFailedException {

     private static final long serialVersionUID = -2345507488342768827L;

     NaverLocationLoadFailedException(NaverLocationErrorType errorType, HttpStatusCode statusCode, String message) {
          super(String.format("[%d] 네이버 | %s : %d - %s",
               errorType.getValue(),
               statusCode.value(),
               message
          ));
     }

     NaverLocationLoadFailedException(NaverLocationErrorType errorType, HttpStatusCode statusCode, String message, Throwable cause) {
          super(String.format("[%d] 네이버 | %s : %d - %s",
               errorType.getValue(),
               statusCode.value(),
               message
          ), cause);
     }
}
