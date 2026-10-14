package com.anseong.core.domain.client.notificationn.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;

public class NotificationFailedException extends GlobalAnseongappException {

     public NotificationFailedException(String message) {
          super(message);
     }

     public NotificationFailedException(String message, Throwable cause) {
          super(message, cause);
     }
}
