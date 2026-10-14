package com.anseong.core.infrastructure.client.certification.iamport;

enum IamportCertificationErrorType {
     CERTIFICATION_FAILED("사용자 인증 실패");

     private final String value;

     IamportCertificationErrorType(String value) {
          this.value = value;
     }

     public String getValue() {
          return value;
     }
}
