package com.anseong.core.infrastructure.aop.fallback;

import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.anseong.core.domain.aop.fallback.FallbackToQueue;
import com.anseong.core.domain.client.schedule.ScheduleClient;
import com.anseong.core.domain.client.schedule.SchedulePlatform;
import com.anseong.core.domain.client.schedule.request.ScheduleMessageQueueRequest;
import com.anseong.core.domain.exception.GlobalAnseongappException;
import com.anseong.global.utils.JsonUtils;

@Aspect
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class AwsFallbackToQueueAspect {

     private static final Logger log = LoggerFactory.getLogger("FALLBACK-QUEUE-LOGGER");

     private final String fallbackArn;
     private final ScheduleClient scheduleClient;
     private final StringRedisTemplate redisTemplate;

     public AwsFallbackToQueueAspect(
          @Value("${api.aws.arn_sqs_fallback}") String fallbackArn,
          ScheduleClient scheduleClient,
          StringRedisTemplate redisTemplate
     ) {
          this.fallbackArn = fallbackArn;
          this.scheduleClient = scheduleClient;
          this.redisTemplate = redisTemplate;
     }

     @Around("@annotation(fallbackToQueue)")
     void scheduleWhenProcessFailed(ProceedingJoinPoint joinPoint, FallbackToQueue fallbackToQueue) {

          try {
               joinPoint.proceed();
          } catch (Throwable e) {

               Object[] args = joinPoint.getArgs();

               if (args.length == 0) {
                    throw new GlobalAnseongappException("!!! FallbackQueue 프로세스 중 데이터 유실 발생, 프로세스 트리거 메세지 : " + e.getMessage());
               }

               LocalDateTime startTime = LocalDateTime.now().plusMinutes(1);
               String methodName = joinPoint.getSignature().getName();
               String scheduleName = methodName + ":" + startTime.toString();
               String queueName = fallbackToQueue.queueName();
               String payload = JsonUtils.toJson(args[0]);

               ScheduleMessageQueueRequest request = ScheduleMessageQueueRequest.builder()
                    .platform(SchedulePlatform.AWS)
                    .scheduleName("FALLBACK-TO-QUEUE:" + scheduleName)
                    .scheduleAt(startTime)
                    .queueName(queueName)
                    .payload(payload)
                    .arn(fallbackArn)
                    .build();

               scheduleClient.scheduleToMessageQueue(request)
                    .thenRun(() -> {
                         log.warn("{} 서킷 브레이커 작동으로 MQ로 프로세스 Payload 발송. 원인 : {}", methodName, e.getMessage());
                    })
                    .exceptionally(e2 -> {
                         String key = methodName + ":" + startTime.toString();
                         redisTemplate.opsForHash().put("fallback:queue:" + methodName, key, JsonUtils.toJson(request));
                         log.error("!!! {} 서킷 브레이커 작동 실패로 Redis에 긴급 적재. Key : {}, 원인 : {}", methodName, key, e2.getMessage());

                         throw new GlobalAnseongappException(methodName + "서킷 브레이커 프로세스 모두 실패 | 첫 원인 : " + e.getMessage() + ", 두번째 원인 : " + e2.getMessage(), e2.getCause());
                    });
          }
     }
}
