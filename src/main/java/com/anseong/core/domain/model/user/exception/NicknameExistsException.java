package com.anseong.core.domain.model.user.exception;

public class NicknameExistsException extends RegistrationException {

     public NicknameExistsException(String message) {
          super(message);
     }

     public NicknameExistsException(String message, Throwable cause) {
          super(message, cause);
     }
}
