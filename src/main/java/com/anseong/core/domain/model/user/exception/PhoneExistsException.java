package com.anseong.core.domain.model.user.exception;

public class PhoneExistsException extends RegistrationException {

    public PhoneExistsException(String message) {
          super(message);
    }

    public PhoneExistsException(String message, Throwable cause) {
          super(message, cause);
    }
}
