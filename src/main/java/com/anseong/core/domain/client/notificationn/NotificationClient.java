package com.anseong.core.domain.client.notificationn;

import com.anseong.core.domain.client.notificationn.request.BatchImportFromAPNsTokenRequest;
import com.anseong.core.domain.client.notificationn.request.NotificationToTokensRequest;
import com.anseong.core.domain.client.notificationn.request.NotificationToTopicRequest;
import com.anseong.core.domain.client.notificationn.request.SubscribeRequest;

public interface NotificationClient {

     void sendToTopic(NotificationToTopicRequest request);

     void sendToTokens(NotificationToTokensRequest request);

     String batchImportFromAPNsToken(BatchImportFromAPNsTokenRequest request);

     void subscribeToTopic(SubscribeRequest request);

     void subscribeToTopicAsync(SubscribeRequest request);

     void unsubscribeFromTopic(SubscribeRequest request);

     void unsubscribeFromTopicAsync(SubscribeRequest request);
}
