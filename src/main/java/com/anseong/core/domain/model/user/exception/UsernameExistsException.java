package com.anseong.core.domain.model.user.exception;

public class UsernameExistsException extends RegistrationException {

     public UsernameExistsException(String message) {
          super(message);
     }

     public UsernameExistsException(String message, Throwable cause) {
          super(message, cause);
     }
}
