package com.anseong.core.web.result;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

public final class Result {
     
     public static ResponseEntity<ApiResult> created() {
          return ResponseEntity.status(201).build();
     }

     public static ResponseEntity<ApiResult> ok() {
          return ResponseEntity.ok().build();
     }

     public static ResponseEntity<ApiResult> accepted() {
          return ResponseEntity.accepted().build();
     }

     public static ResponseEntity<ApiResult> ok(String message) {
          Assert.hasText(message, "파라미터 `message`는 반드시 값이 존재하여야 합니다.");

          return ResponseEntity.ok().body(ApiResult.message(message));
     }

     public static ResponseEntity<ApiResult> ok(ApiResult result) {
          return ResponseEntity.ok().body(result);
	}

     public static ResponseEntity<ApiResult> failure(String message) {
          return ResponseEntity.badRequest().body(ApiResult.message(message));
     }

     public static ResponseEntity<ApiResult> serverError(String message) {
          return ResponseEntity.status(500).body(ApiResult.error(message));
     }

     public static ResponseEntity<ApiResult> notFound() {
          return ResponseEntity.notFound().build();
     }

     public static ResponseEntity<ApiResult> forbidden() {
          return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
     }

     public static ResponseEntity<ApiResult> unauthorized() {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
     }

     public static ResponseEntity<ApiResult> noContent() {
          return ResponseEntity.noContent().build();
     }

     public static ResponseEntity<ApiResult> notModified() {
          return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
     }
}
