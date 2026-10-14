package com.anseong.core.infrastructure.client.certification.iamport;

import org.springframework.http.HttpStatusCode;

import com.anseong.core.domain.client.certification.exception.CertificationFailedException;

class IamportCertificationFailedException extends CertificationFailedException {

     private static final long serialVersionUID = -2749011553888868131L;

     public IamportCertificationFailedException(IamportCertificationErrorType errorType, HttpStatusCode statusCode, String message) {
          super(String.format("아임포트 | %s : %d - %s",
               errorType.getValue(),
               statusCode.value(),
               message
          ));
     }

     public IamportCertificationFailedException(IamportCertificationErrorType errorType, HttpStatusCode statusCode, String message, Throwable cause) {
          super(String.format("[%d] 아임포트 | %s : %d - %s",
               errorType.getValue(),
               statusCode.value(),
               message
          ), cause);
     }
}
