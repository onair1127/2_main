package com.anseong.core.infrastructure.aop.cache;

import java.lang.reflect.Type;
import java.time.Duration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.anseong.core.domain.aop.cache.ResponseCacheable;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.global.utils.JsonUtils;

@Aspect
@Component
class RedisResponseCacheableAspect {

     /**
      * 메모장 봐라
      */
     private final StringRedisTemplate redisTemplate;

     RedisResponseCacheableAspect(
          StringRedisTemplate redisTemplate
     ) {
          this.redisTemplate = redisTemplate;
     }

     @Around("@annotation(responseCacheable)")
     Object processCacheableTask(ProceedingJoinPoint joinPoint, ResponseCacheable responseCacheable) throws Throwable {
          MethodSignature signature = (MethodSignature) joinPoint.getSignature();
          long timeout = responseCacheable.timeoutMinutes();
          String key = generateKey(
               signature.getParameterNames(),
               joinPoint.getArgs(),
               responseCacheable.type()
          );
          if (redisTemplate.hasKey(key)) {
               String result = (String) redisTemplate.opsForValue().get(key);
               Type type = signature.getMethod().getGenericReturnType();
               return JsonUtils.toObject(result, type);

          } else {
               Object result = joinPoint.proceed();

               if (result != null) {
                    String jsonResult = JsonUtils.toJson(result);
                    redisTemplate.opsForValue().set(key, jsonResult, Duration.ofMinutes(timeout));
               }

               return result;
          }
     }

     private String generateKey(String[] parameterNames, Object[] args, String type) {
          String keyPrefix = "cache:api";
          UserId userId = null;
          ImageId imageId = null;

          UserId lastStoreId = null;
          ImageId lastImageId = null;

          if (!type.isEmpty())
               keyPrefix += (":" + type);

          for (int i = 0; i < parameterNames.length; i++) {
               String name = parameterNames[i];

               switch (name) {
                    case "type":
                         keyPrefix += (":" + args[i].toString());
                         break;

                    case "userId":
                    case "storeId":
                         userId = (UserId) args[i];
                         break;

                    case "imageId":
                         imageId = (ImageId) args[i];
                         break;

                    case "lastImageId":
                         lastImageId = (ImageId) args[i];
                         break;

                    case "lastStoreId":
                         lastStoreId = (UserId) args[i];
                         break;
               }
          }

          return keyPrefix
               + (userId != null ? (":" + userId.value()) : "")
               + (imageId != null ? (":" + imageId.value()) : "")
               + (lastStoreId != null ? (":" + lastStoreId.value()) : "")
               + (lastImageId != null ? (":" + lastImageId.value()) : "");
     }
}
