package com.anseong.core.infrastructure.client.notification;

import com.anseong.core.domain.client.notificationn.NotificationClient;
import com.anseong.core.domain.client.notificationn.NotificationPlatform;

public interface InternalNotificationClient extends NotificationClient {
     NotificationPlatform supports();
}
