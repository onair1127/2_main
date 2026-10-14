package com.anseong.core.domain.client.certification.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;

public class CertificationFailedException extends GlobalAnseongappException {

     private static final long serialVersionUID = 5829303129091578812L;

     public CertificationFailedException(String message) {
          super(message);
     }

     public CertificationFailedException(String message, Throwable cause) {
          super(message, cause);
     }
}
