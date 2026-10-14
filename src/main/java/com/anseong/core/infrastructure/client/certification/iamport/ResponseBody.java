package com.anseong.core.infrastructure.client.certification.iamport;

import org.springframework.http.HttpStatus;

import com.anseong.core.domain.client.certification.response.CertificationResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

record IamportPayload<T extends IamportResponseBody>(
     int code,
     String message,
     T response
) {
     public T response() {
          if (code != 0) {
               throw new IamportCertificationFailedException(IamportCertificationErrorType.CERTIFICATION_FAILED, HttpStatus.OK, message);
          }

          return response;
     }
}

record IamportErrorResponse(
     int code,
     String message
) {}

final class ResponseBody {

     @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
     record Certification(
          String impUid,
          String merchantUid,
          String name,
          String birthday,
          String phone
     ) implements BaseIamportResponseBody<CertificationResponse> {

          @Override
          public CertificationResponse toDomainResponse() {
               return new CertificationResponse(birthday, phone);
          }}
}
