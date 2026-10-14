package com.anseong.core.infrastructure.client.notification.lambda;

import java.io.StringReader;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.anseong.core.domain.client.notificationn.NotificationPlatform;
import com.anseong.core.domain.client.notificationn.request.BatchImportFromAPNsTokenRequest;
import com.anseong.core.domain.client.notificationn.request.NotificationToTokensRequest;
import com.anseong.core.domain.client.notificationn.request.NotificationToTopicRequest;
import com.anseong.core.domain.client.notificationn.request.SubscribeRequest;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.infrastructure.client.notification.InternalNotificationClient;
import com.anseong.global.utils.JsonUtils;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

@Component
class LambdaNotificationClient implements InternalNotificationClient {

     private final String FUNCTION_NAME = "fcm";
     private final LambdaClient lambdaClient;

     LambdaNotificationClient(LambdaClient lambdaClient) {
          this.lambdaClient = lambdaClient;
     }

     private final String process(UserId userId, String payload) {
          InvokeRequest request = InvokeRequest
               .builder()
               .functionName(FUNCTION_NAME)
               .payload(SdkBytes.fromUtf8String(payload))
               .build();

          InvokeResponse invokeResponse;
          try {
               invokeResponse = lambdaClient.invoke(request);
          } catch(Exception e) {
               throw new LambdaNotificationSendFailedException("[" + userId.value() + " ] 메세지 요청 실패 : " + e.getMessage(), e.getCause());
          }

          String response = invokeResponse.payload().asUtf8String();
          if (response == null)
               throw new LambdaNotificationSendFailedException("[" + userId.value() + "] 내부 시스템 문제로 응답값이 null 입니다.");

          JsonObject lambdaResult =
               Json.createReader(
                    new StringReader(response)
               ).readObject();

          return lambdaResult.toString();
     }

     public void sendToTopic(NotificationToTopicRequest request) {
          String payload = setPayload("sendToTopic", request);

          this.process(request.userId(), payload);
     }

     @Override
     public void sendToTokens(NotificationToTokensRequest request) {
          String payload = setPayload("sendToTokens", request);

          this.process(request.userId(), payload);
     }

     @Override
     public String batchImportFromAPNsToken(BatchImportFromAPNsTokenRequest request) {
          String payload = setPayload("batchImportFromAPNsToken", request);

          try {
               return this.process(request.userId(), payload);
          } catch (Exception e) {
               throw new LambdaBatchImportTokenFailedException("[" + request.userId().value() + "] APNs 토큰 변환 실패 : " + e.getMessage(), e.getCause());
          }
     }

     @Override
     public void subscribeToTopic(SubscribeRequest request) {
          String payload = setPayload("subscribeToTopic", request);

          this.process(request.userId(), payload);
     }

     @Override
     public void subscribeToTopicAsync(SubscribeRequest request) {
          String payload = setPayload("subscribeToTopicAsync", request);

          this.process(request.userId(), payload);
     }

     @Override
     public void unsubscribeFromTopic(SubscribeRequest request) {
          String payload = setPayload("unsubscribeFromTopic", request);

          this.process(request.userId(), payload);
     }

     @Override
     public void unsubscribeFromTopicAsync(SubscribeRequest request)  {
          String payload = setPayload("unsubscribeFromTopicAsync", request);

          this.process(request.userId(), payload);
     }

     @Override
     public NotificationPlatform supports() {
          return NotificationPlatform.FCM;
     }

     private String setPayload(String type, Object body) {
          Map<String, String> payload = Map.of(
               "type", type,
               "body", JsonUtils.toJson(body)
          );

          return JsonUtils.toJson(payload);
     }
}
