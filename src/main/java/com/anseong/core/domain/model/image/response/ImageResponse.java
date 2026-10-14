package com.anseong.core.domain.model.image.response;

import java.util.Collection;
import java.util.List;

import com.anseong.core.domain.model.image.Image;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.multiple_image.response.MultipleImageResponse;
import com.anseong.core.domain.model.sns.response.SnsResponse;
import com.anseong.core.domain.model.store.response.ImageStoreResponse;
import com.anseong.core.domain.model.tag.response.TagResponse;
import com.anseong.core.domain.model.user.UserId;
import com.anseong.core.domain.model.user.response.ImageUserResponse;

public record ImageResponse(
     ImageId id,
     UserId userId,
     UserId storeId,
     String title,
     int price,
     String description,
     String type,
     boolean job,
     ImageDetailResponse detail,
     ImageUserResponse user,
     ImageStoreResponse store,
     List<TagResponse> tags,
     List<MultipleImageResponse> multipleImages,

     String referenceTagName,
     int favorites,
     long exposedCount
) {
     public ImageResponse(
          Long id,
          Long userId,
          Long storeId,
          String title,
          Integer price,
          String description,
          String type,
          Boolean job,
          ImageDetailResponse detail,
          ImageUserResponse user,
          ImageStoreResponse store,

          String referenceTagName
     ) {
          this(
               new ImageId(id),
               new UserId(userId),
               new UserId(storeId),
               title,
               price,
               description,
               type,
               job,
               detail,
               user,
               store,
               null,
               null,
               referenceTagName,
               0,
               0
          );
     }

     public static ImageResponse from(Image image) {
          return new ImageResponse(
               image.getId(),
               image.getUserId(),
               image.getStoreId(),
               image.getTitle(),
               image.getPrice(),
               image.getDescription(),
               image.getType(),
               image.isJob(),
               ImageDetailResponse.from(image.getDetail()),
               ImageUserResponse.from(image.getUser()),
               ImageStoreResponse.from(image.getStore()),
               TagResponse.from(image.getTags()),
               MultipleImageResponse.from(image.getMultipleImages()),

               null,
               image.getFavorites(),
               image.getExposedCount()
          );
     }

     public static List<ImageResponse> from(Collection<Image> images) {
          return images.stream()
               .map(ImageResponse::from)
               .toList();
     }

     public ImageResponse withCollections(List<SnsResponse> snsResponses, List<TagResponse> tagResponses, List<MultipleImageResponse> multipleImageResponses) {
          return new ImageResponse(
               this.id,
               this.userId,
               this.storeId,
               this.title,
               this.price,
               this.description,
               this.type,
               this.job,
               this.detail,
               this.user,
               this.store.withSns(snsResponses),
               tagResponses,
               multipleImageResponses,
               this.referenceTagName,
               this.favorites,
               this.exposedCount
          );
     }

     public ImageResponse withCacheData(int favorites, long exposedCount) {
          return new ImageResponse(
               this.id,
               this.userId,
               this.storeId,
               this.title,
               this.price,
               this.description,
               this.type,
               this.job,
               this.detail,
               this.user,
               this.store,
               this.tags,
               this.multipleImages,
               this.referenceTagName,
               favorites,
               exposedCount
          );
     }

     public Builder builder() {
          return new Builder(this);
     }

     public static class Builder {
          private ImageResponse original;
          private int favorites;
          private long exposedCount;

          public Builder(ImageResponse original) {
               this.original = original;
               this.favorites = original.favorites;
               this.exposedCount = original.exposedCount;
          }

          public Builder favorites(int favorites) {
               this.favorites = favorites;
               return this;
          }

          public Builder exposedCount(long exposedCount) {
               this.exposedCount = exposedCount;
               return this;
          }

          public ImageResponse build() {
               return new ImageResponse(
                    original.id(),
                    original.userId(),
                    original.storeId(),
                    original.title(),
                    original.price(),
                    original.description(),
                    original.type(),
                    original.job(),
                    original.detail(),
                    original.user(),
                    original.store(),
                    original.tags(),
                    original.multipleImages(),
                    original.referenceTagName(),

                    this.favorites,
                    this.exposedCount
               );
          }
     }
}
