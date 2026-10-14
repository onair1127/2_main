package com.anseong.core.infrastructure.client.convert.media.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.anseong.core.domain.client.convert.media.ConvertMediaPlatform;
import com.anseong.core.domain.client.convert.media.request.ConvertMediaRequest;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;
import com.anseong.core.infrastructure.client.convert.media.ConvertMediaSupport;
import com.anseong.core.infrastructure.client.convert.media.InternalConvertMediaClient;
import com.anseong.global.utils.JsonUtils;

import jakarta.json.Json;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaAsyncClient;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

@Component
class LambdaConvertMediaClient extends ConvertMediaSupport implements InternalConvertMediaClient {

     private static final String VIDEO_FUNCTION = "ffmpeg";
     private static final String IMAGE_FUNCTION = "gm";
     private static final String S3_URL = "anseongapp-images.s3.ap-northeast-2.amazonaws.com";
     private static final String CDN_URL = "image.anseongapp.com";

     private final LambdaClient lambdaClient;
     private final LambdaAsyncClient lambdaAsyncClient;

     LambdaConvertMediaClient(
          LambdaClient lambdaClient,
          LambdaAsyncClient lambdaAsyncClient
     ) {
          this.lambdaClient = lambdaClient;
          this.lambdaAsyncClient = lambdaAsyncClient;
     }

     @Override
     public ConvertMediaResponse process(ConvertMediaRequest request) {
          String mediaType = mediaType(request.ext());

          InvokeRequest invokeRequest = InvokeRequest.builder()
               .functionName(mediaType.equals("video") ? VIDEO_FUNCTION : IMAGE_FUNCTION)
               .payload(payload(request, mediaType))
               .build();

          /**
           * 1. Lambda로 미디어 컨버팅 요청
           */
          InvokeResponse invokeResponse;
          try {
               invokeResponse = lambdaClient.invoke(invokeRequest);
          } catch (Exception e) {
               throw new LambdaConvertMediaFailedException("요청 실패 : " + e.getMessage(), e.getCause());
          }

          /**
           * 2. Lambda 응답 payload가 비어있는지 확인
           */
          String responseString = invokeResponse.payload().asUtf8String();
          if (responseString == null)
               throw new LambdaConvertMediaFailedException("외부 시스템 문제로 응답값이 null 입니다.");

          /**
           * 응답에 에러가 발생했는지 확인
           */
          ConvertMediaResponse response = JsonUtils.toObject(invokeResponse.payload().asUtf8String(), ConvertMediaResponse.class);
          if (response.status().equals("FAIL"))
               throw new LambdaConvertMediaFailedException("미디어 컨버팅 실패 : " + response.errorMessage());

          return response.withSequenceAndUrl(request.sequence(), request.url());
     }

     @Override
     public List<ConvertMediaResponse> process(List<ConvertMediaRequest> requests) {
          List<CompletableFuture<ConvertMediaResponse>> processes = new ArrayList<>();

          for (ConvertMediaRequest request : requests) {
               String mediaType = mediaType(request.ext());

               InvokeRequest invokeRequest = InvokeRequest.builder()
                    .functionName(mediaType.equals("video") ? VIDEO_FUNCTION : IMAGE_FUNCTION)
                    .payload(payload(request, mediaType))
                    .build();

               processes.add(lambdaAsyncClient.invoke(invokeRequest).thenApply(invokeResponse -> {

                    /**
                     * 1. Lambda 응답 payload가 비어있는지 확인
                     */
                    String responseString = invokeResponse.payload().asUtf8String();
                    if (responseString == null)
                         throw new LambdaConvertMediaFailedException("외부 시스템 문제로 응답값이 null 입니다.");

                    /**
                     * 2. 응답에 에러가 발생했는지 확인
                     */
                    ConvertMediaResponse response = JsonUtils.toObject(invokeResponse.payload().asUtf8String(), ConvertMediaResponse.class);
                    if (response.status().equals("FAIL"))
                         throw new LambdaConvertMediaFailedException("미디어 컨버팅 실패 : " + response.errorMessage());

                    return response.withSequenceAndUrl(request.sequence(), request.url());

               }).exceptionally(e -> {
                    throw new LambdaConvertMediaFailedException(e.getMessage(), e.getCause());
               }));
          }

          CompletableFuture.allOf(processes.toArray(new CompletableFuture[0])).join();

          return processes.stream()
               .map(e -> e.join())
               .toList();
     }

     private SdkBytes payload(ConvertMediaRequest request, String mediaType) {
          switch (mediaType) {
               case "image":
                    return SdkBytes.fromUtf8String(
                         Json.createObjectBuilder()
                              .add("username", request.username())
                              .add("filename", request.filename() + "." + request.ext())
                              .add("mode", request.mode().value())
                         .build().toString()
                    );

               case "video":
                    return SdkBytes.fromUtf8String(
                         Json.createObjectBuilder()
                              .add("username", request.username())
                              .add("filename", request.filename())
                              .add("url", request.url().replace(S3_URL, CDN_URL))
                              .add("ext", request.ext())
                         .build().toString()
                    );

               default:
                    throw new LambdaConvertMediaFailedException("미디어 컨버팅 페이로드 생성 실패 - mediaType : " + mediaType);
          }
     }

     @Override
     public ConvertMediaPlatform supports() {
          return ConvertMediaPlatform.LAMBDA;
     }
}
