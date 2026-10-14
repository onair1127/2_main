package com.anseong.core.infrastructure.client.notification.lambda;

import com.anseong.core.domain.client.notificationn.exception.NotificationFailedException;

class LambdaNotificationSendFailedException extends NotificationFailedException {

     private static final long serialVersionUID = -4919241598644126802L;

     LambdaNotificationSendFailedException(String message) {
          super(message);
     }

     LambdaNotificationSendFailedException(String message, Throwable cause) {
          super(message, cause);
     }
}
