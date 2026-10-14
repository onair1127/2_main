package com.anseong.core.web.payloads;

import java.util.List;

import com.anseong.core.application.queries.image.GetImageQuery;

import jakarta.validation.constraints.NotNull;

public class GetImagesPayload {
     @NotNull
     List<String> tagLists;

     @NotNull
     List<Long> imageIds;

     @NotNull
     List<Long> sids;

     String current;

     boolean job;

     double lat;

     double lng;

     public GetImageQuery toQuery() {
          return GetImageQuery.of(tagLists, imageIds, sids, current, job, lat, lng);
     }

     public void setTagLists(List<String> tagLists) {
          this.tagLists = tagLists;
     }

     public void setImageIds(List<Long> imageIds) {
          this.imageIds = imageIds;
     }

     public void setSids(List<Long> sids) {
          this.sids = sids;
     }

     public void setCurrent(String current) {
          this.current = current;
     }

     public void setJob(Boolean job) {
          this.job = job;
     }

     public void setLat(Double lat) {
          this.lat = lat;
     }

     public void setLng(Double lng) {
          this.lng = lng;
     }
}
