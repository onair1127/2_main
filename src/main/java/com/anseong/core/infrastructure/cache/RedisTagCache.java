package com.anseong.core.infrastructure.cache;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.tag.repository.TagCache;

@Repository
public class RedisTagCache implements TagCache {

     private final StringRedisTemplate redisTemplate;

     public RedisTagCache(StringRedisTemplate redisTemplate) {
          this.redisTemplate = redisTemplate;
     }

     /**
      * 운영자 추천 태그 추가
      * @param tag
      * @param description
      */
     @Override
     @Async
     public void addRecommendTag(String tag, String description) {
          final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

          hashOperations.put("recommend:tag", tag, description);
     }

     @Override
     public void deleteRecommendTag(String tag) {
          final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

          hashOperations.delete("recommend:tag", tag);
     }

     /**
      * 첫 메인에서 사진 추천을 위한 운영자 추천 태그 중 랜덤으로 '3'개 가져오기
      * @return
      */
     @Override
     public Map<String, String> getRandomRecommendTags() {
          final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

          Map<String, String> results = new LinkedHashMap<>();
          List<String> tags = hashOperations.randomKeys("recommend:tag", 2);

          if (tags == null)
               return Map.of();

          for (String tag : tags) {
               results.put(tag, hashOperations.get("recommend:tag", tag));
          }

          results.put("영업중", "현재 영업중인 가게들만 보기");

          return results;
     }

     /**
      * 전체, 나이대별 태그 순위 매기기
      * @param tagLists
      */
     @Async
     public void determineTag(List<String> tagLists) {
          redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
               for (String tag : tagLists) {
                    byte[] rankingTag = "ranking:tag".getBytes();
                    byte[] rankingMonthlyTag = "ranking:monthly:tag".getBytes();

                    connection.zSetCommands().zIncrBy(rankingTag, 1, tag.getBytes());
                    connection.zSetCommands().zIncrBy(rankingMonthlyTag, 1, tag.getBytes());
               }

               return null;
          });
     }
}
