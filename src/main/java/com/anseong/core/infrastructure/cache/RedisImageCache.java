package com.anseong.core.infrastructure.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.anseong.core.domain.model.image.Image;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.repository.ImageCache;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.user.UserId;

@Repository
public class RedisImageCache implements ImageCache {

     private final StringRedisTemplate redisTemplate;

     public RedisImageCache(StringRedisTemplate redisTemplate) {
          this.redisTemplate = redisTemplate;
     }

     /**
      * 사진들의 Favorites 가져오기
      * @param images
      * @return
      */
     public List<Image> setImageFavorites(final List<Image> images) {
          if (images.size() > 0) {
               final ValueOperations<String, String> operations = redisTemplate.opsForValue();

               List<String> keys = images.stream()
                    .map(i -> "image:favorite:" + i.getId().value())
                    .toList();

               List<String> favoritesList = operations.multiGet(keys);

               if (favoritesList != null) {
                    for (int i = 0; i < images.size(); i++) {
                         String favorites = favoritesList.get(i);
                         images.get(i).setFavorites(favorites != null ? Integer.parseInt(favorites) : 0);
                    }
               }
          }

          return images;
     }

     @Override
     public List<ImageResponse> setImageFavoritesFromCachedImages(List<ImageResponse> cachedImages) {
          if (cachedImages.size() > 0) {
               final ValueOperations<String, String> operations = redisTemplate.opsForValue();

               List<String> keys = cachedImages.stream()
                    .map(i -> "image:favorite:" + i.id().value())
                    .toList();

               List<String> favoritesList = operations.multiGet(keys);
               List<ImageResponse> result = new ArrayList<>();

               if (favoritesList != null) {
                    for (int i = 0; i < cachedImages.size(); i++) {
                         String favorites = favoritesList.get(i);
                         result.add(cachedImages.get(i).builder()
                              .favorites(favorites != null ? Integer.parseInt(favorites) : 0)
                              .build()
                         );
                    }
               }

               return result;
          } else {
               return cachedImages;
          }
     }

     @Override
     public ImageResponse setImageFavoritesAndExposedCountOne(ImageResponse image) {
          List<Object> pipelineResults = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
               Long imageId = image.id().value();
               Long userId = image.userId().value();

               byte[] favoritesKey = ("image:favorite:" + imageId).getBytes();
               byte[] exposedCountKey = ("expose:image:" + userId).getBytes();
               byte[] storeValue = ("[" + image.storeId() + "] " + image.store().storeName()).getBytes();

               connection.stringCommands().get(favoritesKey);

               connection.hashCommands().hIncrBy(
                    exposedCountKey,
                    imageId.toString().getBytes(),
                    1
               );

               connection.hashCommands().hIncrBy(
                    exposedCountKey,
                    "total".getBytes(),
                    1
               );

               connection.zSetCommands().zIncrBy(
                    "expose:image:all".getBytes(),
                    1,
                    storeValue
               );

               return null;
          });

          List<String> commandResults = pipelineResults.stream()
               .map(c -> c == null ? "0" : c.toString())
               .collect(Collectors.toList());

          int favorites = Integer.parseInt(commandResults.get(0));
          long exposedCount = Long.parseLong(commandResults.get(1));

          return image.withCacheData(favorites, exposedCount);
     }

     @Override
     @Async
     public void addFavorite(final UserId userId, final ImageId imageId) {
          final ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
          final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

          if (userId.value() != 0) {
               ListOperations<String, String> listOperations = redisTemplate.opsForList();

               listOperations.remove("user:favorite:" + userId.value(), 0, imageId.value().toString());
               listOperations.leftPush("user:favorite:" + userId.value(), imageId.value().toString());
          }

          valueOperations.increment("image:favorite:" + imageId.value());
          zSetOperations.incrementScore("ranking:favorite", imageId.value().toString(), 1);
          zSetOperations.incrementScore("ranking:monthly:favorite", imageId.value().toString(), 1);
     }

     @Override
     @Async
     public void deleteFavorite(final UserId userId, final ImageId imageId) {
          final ListOperations<String, String> listOperations = redisTemplate.opsForList();
          final ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
          final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

          valueOperations.decrement("image:favorite:" + imageId.value());
          listOperations.remove("user:favorite:" + userId.value(), 0, imageId.value().toString());
          zSetOperations.incrementScore("ranking:favorite", imageId.value().toString(), -1);
     }

     @Override
     public void deleteFavoriteAll(ImageId imageId) {
          final ListOperations<String, String> listOperations = redisTemplate.opsForList();
          final ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
          final Set<String> favorites = redisTemplate.keys("user:favorite:*");

          for (String key : favorites) {
               listOperations.remove(key, 0, imageId.value().toString());
          }

          redisTemplate.delete("image:favorite:" + imageId.value());
          zSetOperations.remove("ranking:favorite", imageId.value().toString());
     }

     @Override
     public List<Image> setExposedCount(List<Image> images) {
          if (images.size() > 0) {
               List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                    for (Image i : images) {
                         String storeValue = "[" + i.getUserId().value() + "] " + i.getStore().getStoreName();

                         connection.hashCommands().hIncrBy(
                              ("expose:image:" + i.getUserId().value()).getBytes(),
                              i.getId().value().toString().getBytes(),
                              1
                         );

                         connection.hashCommands().hIncrBy(
                              ("expose:image:" + i.getUserId().value()).getBytes(),
                              "total".getBytes(),
                              1
                         );

                         connection.zSetCommands().zIncrBy(
                              "expose:image:all".getBytes(),
                              1,
                              storeValue.getBytes()
                         );
                    }

                    return null;
               });

               for (int i = 0; i < images.size(); i++) {
                    String exposedCount = String.valueOf(results.get(i*3));
                    images.get(i).setExposedCount(exposedCount != null ? Long.parseLong(exposedCount) : 0);
               }
          }

          return images;
     }

     @Override
     public List<ImageResponse> setExposedCountFromCachedImages(List<ImageResponse> cachedImages) {
          if (cachedImages.size() > 0) {
               List<Object> commandResults = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                    for (ImageResponse i : cachedImages) {
                         String storeValue = "[" + i.userId().value() + "] " + i.store().storeName();

                         connection.hashCommands().hIncrBy(
                              ("expose:image:" + i.userId().value()).getBytes(),
                              i.id().value().toString().getBytes(),
                              1
                         );

                         connection.hashCommands().hIncrBy(
                              ("expose:image:" + i.userId().value()).getBytes(),
                              "total".getBytes(),
                              1
                         );

                         connection.zSetCommands().zIncrBy(
                              "expose:image:all".getBytes(),
                              1,
                              storeValue.getBytes()
                         );
                    }

                    return null;
               });

               List<ImageResponse> results = new ArrayList<>();

               for (int i = 0; i < cachedImages.size(); i++) {
                    String exposedCount = String.valueOf(commandResults.get(i*3));
                    results.add(cachedImages.get(i).builder()
                         .exposedCount(exposedCount != null ? Long.parseLong(exposedCount) : 0)
                         .build()
                    );
               }

               return results;
          } else {
               return cachedImages;
          }
     }

     @Override
     public List<Image> setImageFavoritesAndExposedCount(List<Image> images) {
          // "image:favorite:" + i.getId().value()

          if (images.size() > 0) {
               List<Object> commandResults = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                    for (Image i : images) {
                         Long imageId = i.getId().value();
                         Long userId = i.getUserId().value();

                         byte[] favoritesKey = ("image:favorite:" + imageId).getBytes();
                         byte[] exposedCountKey = ("expose:image:" + userId).getBytes();
                         byte[] storeValue = ("[" + i.getUserId().value() + "] " + i.getStore().getStoreName()).getBytes();

                         connection.stringCommands().get(favoritesKey);

                         connection.hashCommands().hIncrBy(
                              exposedCountKey,
                              imageId.toString().getBytes(),
                              1
                         );

                         connection.hashCommands().hIncrBy(
                              exposedCountKey,
                              "total".getBytes(),
                              1
                         );

                         connection.zSetCommands().zIncrBy(
                              "expose:image:all".getBytes(),
                              1,
                              storeValue
                         );
                    }

                    return null;
               });

               commandResults = commandResults.stream()
                    .map(c -> c == null ? "0" : c.toString())
                    .collect(Collectors.toList());

               for (int i = 0; i < images.size(); i++) {
                    int index = i*4;

                    String favorites = String.valueOf(commandResults.get(index));
                    String exposedCount = String.valueOf(commandResults.get(index+1));

                    images.get(i).setFavorites(Integer.parseInt(favorites));
                    images.get(i).setExposedCount(Long.parseLong(exposedCount));
               }
          }

          return images;
     }

     @Override
     public List<ImageResponse> setImageFavoritesAndExposedCountFromResponse(List<ImageResponse> images) {
          if (images.size() > 0) {
               List<Object> commandResults = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                    for (ImageResponse i : images) {
                         Long imageId = i.id().value();
                         Long userId = i.userId().value();

                         byte[] favoritesKey = ("image:favorite:" + imageId).getBytes();
                         byte[] exposedCountKey = ("expose:image:" + userId).getBytes();
                         byte[] storeValue = ("[" + i.userId().value() + "] " + i.store().storeName()).getBytes();

                         connection.stringCommands().get(favoritesKey);

                         connection.hashCommands().hIncrBy(
                              exposedCountKey,
                              imageId.toString().getBytes(),
                              1
                         );

                         connection.hashCommands().hIncrBy(
                              exposedCountKey,
                              "total".getBytes(),
                              1
                         );

                         connection.zSetCommands().zIncrBy(
                              "expose:image:all".getBytes(),
                              1,
                              storeValue
                         );
                    }

                    return null;
               });

               commandResults = commandResults.stream()
                    .map(c -> c == null ? "0" : c.toString())
                    .collect(Collectors.toList());

               List<ImageResponse> results = new ArrayList<>();
               for (int i = 0; i < images.size(); i++) {
                    int index = i*4;

                    int favorites = Integer.parseInt(String.valueOf(commandResults.get(index)));
                    long exposedCount = Long.parseLong(String.valueOf(commandResults.get(index+1)));

                    // images.get(i).setFavorites(Integer.parseInt(favorites));
                    // images.get(i).setExposedCount(Long.parseLong(exposedCount));
                    results.add(images.get(i).withCacheData(favorites, exposedCount));
               }

               return results;

          } else {
               return images;
          }
     }

     @Override
     @Async
     public void incrementClickShareCount(UserId storeId, ImageId imageId) {
          final HashOperations<String, String, String> operations = redisTemplate.opsForHash();

          operations.increment("click:share" + storeId.value(), "image:" + imageId.value(), 1);
     }

     @Override
     @Async
     public void setExposedShareCount(Image image) {
          final HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

          hashOperations.increment("expose:share:" + image.getUserId().value(), "total", 1);
          hashOperations.increment("expose:share:" + image.getUserId().value(), image.getId().value().toString(), 1);
     }
}
