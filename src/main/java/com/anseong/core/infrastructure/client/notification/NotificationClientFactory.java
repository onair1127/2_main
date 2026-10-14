package com.anseong.core.infrastructure.client.notification;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import com.anseong.core.domain.client.notificationn.NotificationClient;
import com.anseong.core.domain.client.notificationn.NotificationPlatform;
import com.anseong.core.domain.client.notificationn.exception.BatchImportTokenFailedException;
import com.anseong.core.domain.client.notificationn.exception.NotificationFailedException;
import com.anseong.core.domain.client.notificationn.request.BatchImportFromAPNsTokenRequest;
import com.anseong.core.domain.client.notificationn.request.NotificationToTokensRequest;
import com.anseong.core.domain.client.notificationn.request.NotificationToTopicRequest;
import com.anseong.core.domain.client.notificationn.request.SubscribeRequest;

@Component
@Primary
@Retryable (
     includes = { ResourceAccessException.class, NotificationFailedException.class, BatchImportTokenFailedException.class },
     maxRetries = 3,
     maxDelay = 3
)
class NotificationClientFactory implements NotificationClient {

     private final Map<NotificationPlatform, NotificationClient> clients;
     private final NotificationPlatform DEFAULT_PLATFORM = NotificationPlatform.FCM;

     NotificationClientFactory(List<InternalNotificationClient> messageClients) {
          this.clients = messageClients.stream()
               .collect(Collectors.toMap(
                    InternalNotificationClient::supports,
                    Function.identity()
               ));
     }

     private NotificationClient getClient(NotificationPlatform platform) {
          if (platform == null)
               platform = DEFAULT_PLATFORM;

          return clients.get(platform);
     }

     @Override
     public void sendToTopic(NotificationToTopicRequest request) {
          getClient(request.platform()).sendToTopic(request);
     }

     @Override
     public void sendToTokens(NotificationToTokensRequest request) {
          getClient(request.platform()).sendToTokens(request);
     }

     @Override
     public String batchImportFromAPNsToken(BatchImportFromAPNsTokenRequest request) {
          return getClient(request.platform()).batchImportFromAPNsToken(request);
     }

     @Override
     public void subscribeToTopic(SubscribeRequest request) {
          getClient(request.platform()).subscribeToTopic(request);
     }

     @Override
     public void subscribeToTopicAsync(SubscribeRequest request) {
          getClient(request.platform()).subscribeToTopicAsync(request);
     }

     @Override
     public void unsubscribeFromTopic(SubscribeRequest request) {
          getClient(request.platform()).unsubscribeFromTopic(request);
     }

     @Override
     public void unsubscribeFromTopicAsync(SubscribeRequest request) {
          getClient(request.platform()).unsubscribeFromTopicAsync(request);
     }
}
