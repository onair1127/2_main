package com.anseong.core.infrastructure.aop.cache;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.anseong.core.domain.aop.cache.ResponseCacheEvict;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.user.UserId;

@Aspect
@Component
class RedisResponseCacheEvictAspect {

     private final StringRedisTemplate redisTemplate;

     RedisResponseCacheEvictAspect(StringRedisTemplate redisTemplate) {
          this.redisTemplate = redisTemplate;
     }

     @After("@annotation(responseCacheEvict)")
     public void afterDataChanged(JoinPoint joinPoint, ResponseCacheEvict redisCacheEvict) {
          MethodSignature signature = (MethodSignature) joinPoint.getSignature();
          String[] parameterNames = signature.getParameterNames();
          Object[] args = joinPoint.getArgs();
          String keyPrefix = "cache:api:";
          String type = redisCacheEvict.type();
          UserId userId = null;
          ImageId imageId = null;

          for (int i = 0; i < parameterNames.length; i++) {
               if (parameterNames[i].equals("userId"))
                    userId = (UserId) args[i];

               else if (parameterNames[i].equals("storeId"))
                    userId = (UserId) args[i];

               else if (parameterNames[i].equals("imageId"))
                    imageId = (ImageId) args[i];

               else if (parameterNames[i].equals("type"))
                    type = args[i].toString();
          }

          if (userId != null) {
               switch (type) {
                    case "store":
                         redisTemplate.delete(keyPrefix + "store:" + userId.value());
                         break;

                    case "menu":
                         redisTemplate.delete(
                              redisTemplate.keys(keyPrefix + "storeImage:menu:" + userId.value() + ":*")
                         );
                         break;

                    case "info":
                         redisTemplate.delete(
                              redisTemplate.keys(keyPrefix + "storeImage:info:" + userId.value() + ":*")
                         );
                         redisTemplate.delete(
                              redisTemplate.keys(keyPrefix + "info:*")
                         );
                         break;

                    case "review":
                         redisTemplate.delete(
                              redisTemplate.keys(keyPrefix + "storeImage:review:" + userId.value() + ":*")
                         );
                         redisTemplate.delete(
                              redisTemplate.keys(keyPrefix + "review:*")
                         );
                         break;

                    default:
                         if (imageId != null) {
                              redisTemplate.delete("cache:api:share:" + imageId.value());
                              redisTemplate.delete("cache:api:shareTemplate:" + imageId.value());
                         }
                         break;

               }
          }
     }
}
