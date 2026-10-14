package com.anseong.core.domain.model.image.repository;

import java.util.List;

import com.anseong.core.domain.model.image.Image;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.image.response.ImageResponse;
import com.anseong.core.domain.model.user.UserId;

public interface ImageCache {

     /**
      * 사진들의 Favorites 가져오기
      * @param images
      */
     List<Image> setImageFavorites(List<Image> images);

     /**
      * 캐싱된 사진들의 Favorites 가져오기
      * @param cachedImages
      * @return
      */
     List<ImageResponse> setImageFavoritesFromCachedImages(List<ImageResponse> cachedImages);

     /**
      * 홈에서 가져온 사진 한장의 Favorite과 노출수 가져오기
      * @param image
      * @return
      */
     ImageResponse setImageFavoritesAndExposedCountOne(ImageResponse image);

     /**
      * 사진 리스트 추가
      * @param userId
      * @param imageId
      */
     void addFavorite(UserId userId, ImageId imageId);

     /**
      * 개인 리스트 삭제
      * @param userId
      * @param imageId
      */
     void deleteFavorite(UserId userId, ImageId imageId);

     /**
      * 사장님이 사진 삭제하신 이후 모든 유저들의 FavoriteList 목록에서 ImageId를 삭제합니다.
      * @param imageId
      */
     void deleteFavoriteAll(ImageId imageId);

     /**
      * <p>사진이 노출된 가게 수를 기록합니다.</p>
      */
     List<Image> setExposedCount(List<Image> images);

     /**
      * <p>캐싱된 사진으로부터 노출된 가게 수를 기록합니다.</p>
      */
     List<ImageResponse> setExposedCountFromCachedImages(List<ImageResponse> cachedImages);

     /**
      * <p>사진들의 Favorites과 노출수를 기록합니다.</p>
      * @param image
      * @return
      */
     List<Image> setImageFavoritesAndExposedCount(List<Image> images);

     /**
      * <p>사진들의 Favorites과 노출수를 기록합니다.</p>
      * @param image
      * @return
      */
     List<ImageResponse> setImageFavoritesAndExposedCountFromResponse(List<ImageResponse> images);

     void incrementClickShareCount(UserId userId, ImageId imageId);

     /**
      * <p>공유 사진을 클릭한 조회수를 기록합니다.</p>
      */
     void setExposedShareCount(Image image);
}
