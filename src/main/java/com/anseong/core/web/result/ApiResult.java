package com.anseong.core.web.result;

import java.util.HashMap;

import org.springframework.util.Assert;

public class ApiResult extends HashMap<String, Object> {

     private static final long serialVersionUID = 6967248440544684582L;

     private static final String MESSAGE_KEY = "message";

     public static ApiResult blank() {
          return new ApiResult();
     }

     public static ApiResult error(String message) {
          Assert.hasText(message, "파라미터 `message`는 반드시 값이 존재하여야 합니다.");

          ApiResult apiResult = new ApiResult();
          apiResult.put(MESSAGE_KEY, message);

          return apiResult;
     }
     
     public static ApiResult message(String message) {
          Assert.hasText(message, "파라미터 `message`는 반드시 값이 존재하여야 합니다.");

          ApiResult apiResult = new ApiResult();
          apiResult.put("message", message);
          return apiResult;
     }

     public ApiResult add(String key, Object value) {
          Assert.hasText(key, "파라미터 `key`는 반드시 값이 존재하여야 합니다.");
          // Assert.notNull(value, "파라미터 `value`는 반드시 null값이 아니어야 합니다.");

          this.put(key, value);
          return this;
     }

}
