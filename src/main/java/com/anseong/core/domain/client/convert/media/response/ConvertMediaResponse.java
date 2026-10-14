package com.anseong.core.domain.client.convert.media.response;

public record ConvertMediaResponse(
     String status,
     String errorMessage,
     String originalSrcUrl,
     String thumbnail,
     String card,
     String poster,
     int sequence
) {
     public ConvertMediaResponse withSequenceAndUrl(int sequence, String originalSrcUrl) {
          return new ConvertMediaResponse(
               this.status,
               this.errorMessage,
               originalSrcUrl,
               this.thumbnail,
               this.card,
               this.poster,
               sequence
          );
     }
}
