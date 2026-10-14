package com.anseong.core.domain.client.notificationn;

public enum NotificationTopic {
     BOARD("board"),
     CEO("ceo");

     private final String value;

     NotificationTopic(String value) {
          this.value = value;
     }

     public String value() {
          return value;
     }
}
