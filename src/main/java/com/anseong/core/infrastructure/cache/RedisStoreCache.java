package com.anseong.core.infrastructure.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.store.repository.StoreCache;
import com.anseong.core.domain.model.user.UserId;

@Repository
public class RedisStoreCache implements StoreCache {

     private final StringRedisTemplate redisTemplate;

     public RedisStoreCache(StringRedisTemplate redisTemplate) {
          this.redisTemplate = redisTemplate;
     }

     /**
      * 가게 생성 이후 추가 캐싱 작업
      * @param userId
      * @param addSns
      * @param openTime
      */
     @Override
     public void createStore(UserId userId, String storeName, List<String> addSns, double lat, double lng) {
          final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
          final ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
          final HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
          final GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();

          final GeoLocation<String> location = new GeoLocation<String>(String.valueOf(userId.value()), new Point(lng, lat));
          final String storeValue = "[" + userId.value() + "] " + storeName;

          // 가게 Geo 등록
          geoOperations.add("geo_store", location);

          // SNS 추가 캐싱
          if (addSns.size() > 0) {
               hashOperations.put("addSns", String.valueOf(userId.value()),
                         addSns.toString().replace("[", "").replace("]", "").replaceAll(" ", ""));
          }

          // 사진 노출수, 가게 클릭수 캐싱 Initialize
          zSetOperations.add("expose:image:all", storeValue, 0);
          hashOperations.put("expose:image:" + userId.value(), "total", "0");
          valueOperations.set("expose:store:" + userId.value(), "0");
     }

     /**
      * 추가 작업할 가게 SNS목록의 ID들 가져오기
      * @return
      */
     @Override
     public Set<String> getArchivedSnsIds() {
          final HashOperations<String, String, Object> operations = redisTemplate.opsForHash();

          return operations.keys("addSns");
     }

     /**
      * 추가 작업할 가게 SNS목록 가져오기
      * @param storeId
      * @return
      */
     @Override
     public List<String> getArchivedSns(UserId storeId) {
          final HashOperations<String, String, String> operations = redisTemplate.opsForHash();

          String archivedSnsResponse = operations.get("addSns", String.valueOf(storeId.value()));

          if (archivedSnsResponse == null || archivedSnsResponse.isEmpty())
               return List.of();
          else
               return List.of(archivedSnsResponse.split(","));
     }

     /**
      * SNS 추가 작업 완료 후 addSns 목록에서 제거
      * @param id
      */
     public void completeSnsJob(UserId id) {
          final HashOperations<String, String, String> operations = redisTemplate.opsForHash();

          operations.delete("addSns", String.valueOf(id.value()));
     }

     /**
      * 유저 검색기록 추가, 최대 10개까지이며 가장 오래된 순으로 Pop
      * @param userId
      * @param keyword
      */
     @Override
     @Async
     public void addSearchHistory(UserId userId, String keyword) {
          if (userId.value() != 0) {
               final ListOperations<String, String> operations = redisTemplate.opsForList();

               operations.remove("history:search:" + userId.value(), 0, keyword);

               if (operations.size("history:search:" + userId.value()) == 10) {
                    operations.rightPop("history:search:" + userId.value());
               }

               operations.leftPush("history:search:" + userId.value(), keyword);
          }
     }

     /**
      * 유저 검색기록 가져오기
      * @param userId
      * @return
      */
     @Override
     public List<String> getSearchHistory(UserId userId) {
          if (userId.value() != 0) {
               final ListOperations<String, String> operations = redisTemplate.opsForList();

               return operations.range("history:search:" + userId.value(), 0, -1);
          } else {
               return new ArrayList<>();
          }
     }

     /**
      * 유저 검색기록 삭제
      * @param userId
      * @param history
      */
     @Override
     @Async
     public void deleteSearchHistory(UserId userId, String history) {
          if (userId.value() != 0) {
               final ListOperations<String, String> operations = redisTemplate.opsForList();

               operations.remove("history:search:" + userId.value(), 0, history);
          }
     }

     @Override
     public void deleteStore(UserId userId) {
          final GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();

          geoOperations.remove("geo_store", String.valueOf(userId.value()));
     }

     @Override
     public List<UserId> getGeoStoreIds(Double lat, Double lng) {
          final GeoOperations<String, String> operations = redisTemplate.opsForGeo();
          final Circle circle = new Circle(new Point(lng, lat), new Distance(3, Metrics.KILOMETERS));
          final GeoResults<GeoLocation<String>> geoResponses = operations.radius("geo_store", circle);

          List<GeoResult<GeoLocation<String>>> geoResults;

          if (geoResponses == null)
               geoResults = List.of();
          else
               geoResults = geoResponses.getContent();

          List<UserId> results = new ArrayList<>();

          for (GeoResult<GeoLocation<String>> r : geoResults) {
               GeoLocation<String> result = r.getContent();
               results.add(new UserId(Long.parseLong(result.getName())));
          }

          return results;
     }

     @Override
     public void updateGeoLocation(UserId userId, double lat, double lng) {
          final GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
          final GeoLocation<String> location = new GeoLocation<String>(String.valueOf(userId.value()), new Point(lng, lat));

          geoOperations.add("geo_store", location);
     }

     @Override
     @Async
     public void incrementClickStoreCount(UserId storeId) {
          final HashOperations<String, String, String> operations = redisTemplate.opsForHash();

          operations.increment("click:" + storeId.value(), "store", 1);
     }

     @Override
     public void restoreGeoStore(List<Map<String, Object>> dataSet) {
          final GeoOperations<String, String> geoOperations = redisTemplate.opsForGeo();
          final List<GeoLocation<String>> locations = new ArrayList<>();

          redisTemplate.delete("geo_store");

          for (Map<String, Object> data : dataSet) {
               GeoLocation<String> location = new GeoLocation<String>(String.valueOf(data.get("userId")), new Point((double) data.get("lng"), (double) data.get("lat")));
               locations.add(location);
          }

          // 가게 Geo 등록
          geoOperations.add("geo_store", locations);
     }

     @Override
     @Async
     public void incrementClickSnsCount(UserId storeId, String type) {
          final HashOperations<String, String, String> operations = redisTemplate.opsForHash();

          operations.increment("click:sns:" + storeId.value(), type, 1);
     }
}
