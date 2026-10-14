package com.anseong.core.infrastructure.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.repository.UserCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
public class RedisUserCache implements UserCache {

     private static final Logger log = LoggerFactory.getLogger(RedisUserCache.class);

     private final StringRedisTemplate redisTemplate;

     public RedisUserCache(StringRedisTemplate redisTemplate) {
          this.redisTemplate = redisTemplate;
     }

     @Override
     public boolean addUserFcmToken(UserId userId, String token, boolean isCeo, boolean adult) {
          final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
          final SetOperations<String, String> setOperations = redisTemplate.opsForSet();

          /**
           * 비회원일경우 구독만 하기(subscribe:board, subscribeToTopic)
           */
          if (userId.value() == 0) {
               log.info("[GUEST] 신규 비회원 유저 운영자 소식 구독");
               setOperations.add("subscribe:board", token);
               return false;
          }

          final String key = isCeo ? "fcm:ceo" : "fcm:user";
          boolean hasKey = hashOperations.hasKey(key, userId.value().toString());

          if (!hasKey) {
               log.info("[{}] 신규 유저, 운영자 소식 구독", userId.value());
               setOperations.add("subscribe:board", token);
               hashOperations.put(key, userId.value().toString(), token);
               return false;

          } else {
               String userTokens = hashOperations.get(key, userId.value().toString());

               if (userTokens != null && Arrays.asList(userTokens.split(", ")).contains(token) ) {
                    log.info("[{}] 기존 유저, 그러나 기기값은 같으므로 그냥 패스", userId.value());
                    return false;
               } else {
                    log.info("[{}] 기존 유저, 토큰값 다르므로 신규 기기 감지", userId.value());
                    String currentTokens = hashOperations.get(key, userId.value().toString());
                    hashOperations.put(key, userId.value().toString(), currentTokens + ", " + token);
                    return true;
               }
          }
     }

     @Override
     public Set<String> getCeoFcmTokenByIds(Collection<UserId> ids) {
          final HashOperations<String, String, String> operations = redisTemplate.opsForHash();
          Set<String> results = new HashSet<>();

          for (UserId id : ids) {
               String token = operations.get("fcm:ceo", id.value().toString());

               if (token != null)
                    results.add(operations.get("fcm:ceo", id.value().toString()));
          }

          return results;
     }

     @Override
     public String deleteUserFcmToken(UserId userId, boolean isCeo) {
          final HashOperations<String, String, String> operations = redisTemplate.opsForHash();
          final String key = isCeo ? "fcm:ceo" : "fcm:user";
          final String token = operations.get(key, userId.value().toString());

          if (token != null)
               operations.delete(key, userId.value().toString());

          return token;
     }

     @Override
     public void subscribeBoard(String token) {
          final SetOperations<String, String> operations = redisTemplate.opsForSet();
          operations.add("subscribe:board", token);
     }

     @Override
     public void unsubscribeBoard(String token) {
          final SetOperations<String, String> operations = redisTemplate.opsForSet();
          operations.remove("subscribe:board", token);
     }

     @Override
     public boolean getSubscribeBoardStatus(String token) {
          final SetOperations<String, String> operations = redisTemplate.opsForSet();
          Boolean isMember = operations.isMember("subscribe:board", token);

          return isMember == null ? false : isMember;
     }

     @Override
     public Map<String, List<Integer>> getUserData(UserId userId) {
          final ListOperations<String, String> listOperations = redisTemplate.opsForList();
          final SetOperations<String, String> setoperations = redisTemplate.opsForSet();
          Map<String, List<Integer>> results = new HashMap<>();

          List<String> favoriteList = listOperations.range("user:favorite:" + userId.value(), 0, -1);
          Set<String> boardLikeList = setoperations.members("user:likeBoard:" + userId.value());

          if (favoriteList != null)
               results.put("favoriteList", favoriteList
                    .stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList()));

          if (boardLikeList != null)
               results.put("boardLikeList", boardLikeList
                    .stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList()));

          return results;
     }

     @Override
     @Async
     public void setImageSort(UserId userId, int imageSort) {
          final HashOperations<String, String, String> operations = redisTemplate.opsForHash();

          operations.put("user:imageSort", String.valueOf(userId.value()), String.valueOf(imageSort));
     }

     @Override
     public int getImageSort(UserId userId) {
          final HashOperations<String, String, String> operations = redisTemplate.opsForHash();
          final String imageSort = operations.get("user:imageSort", String.valueOf(userId.value()));

          return imageSort != null ? Integer.parseInt(imageSort) : 3;
     }

     @Override
     @Async
     public void addUserDataFromGuestData(Collection<String> favoriteList, Collection<String> boardLikeList, Collection<String> searchHistory, int imageSort, UserId userId) {
          final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
          final ListOperations<String, String> listOperations = redisTemplate.opsForList();
          final SetOperations<String, String> setoperations = redisTemplate.opsForSet();

          if (favoriteList.size() > 0)
               listOperations.rightPushAll("user:favorite:" + userId.value(), favoriteList);

          if (boardLikeList.size() > 0)
               setoperations.add("user:likeBoard:" + userId.value(), boardLikeList.toArray(new String[favoriteList.size()]));

          if (searchHistory.size() > 0)
               listOperations.rightPushAll("history:search:" + userId.value(), favoriteList);

          if (imageSort != 3)
               hashOperations.put("user:imageSort", String.valueOf(userId.value()), String.valueOf(imageSort));
     }
}
