package com.anseong.core.domain.model.user.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;

public class RegistrationException extends GlobalAnseongappException {

     public RegistrationException(String message) {
          super(message);
     }

     public RegistrationException(String message, Throwable cause) {
          super(message, cause);
     }
}
