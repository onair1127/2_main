package com.anseong.core.infrastructure.client.location.naver;

enum NaverLocationErrorType {
     GEOCODE_LOAD_FAILED("Geocode 로드 실패"),
     REVERSE_GEOCODE_LOAD_FAILED("Reverse Geocode 로드 실패");

     private final String value;

     NaverLocationErrorType(String value) {
          this.value = value;
     }

     public String getValue() {
          return value;
     }
}
