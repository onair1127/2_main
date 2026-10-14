package com.anseong.core.web.payloads;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

import com.anseong.core.application.commands.store.UpdateMyStoreCommand;

public class UpdateMyStorePayload {

     @NotNull
     @NotEmpty(message = "가게 이름이 비어있습니다.")
     @Size(max = 16, message = "가게 이름이 너무 깁니다.")
     private String name;

     private String phone;

     @NotNull
     @NotEmpty(message = "가게 주소가 비어있습니다.")
     private String address;

     @NotNull
     @Min(value = 10, message = "lat 값이 비어있습니다.")
     private double lat;

     @NotNull
     @Min(value = 10, message = "lng 값이 비어있습니다.")
     private double lng;

     @NotNull
     private String description;

     @NotNull
     private String announcement;

     @NotNull
     @NotEmpty
     private String thumbnail;

     @NotNull
     private List<Integer> breakDates;

     @NotNull
     private int breakStart;

     @NotNull
     private int breakEnd;

     @NotNull
     private boolean adult;

     @NotNull
     private boolean thumbnailUpdated;

     private String filter;

     private String menuLists;

     private List<Map<String, String>> addSns;

     @NotNull
     private Map<String, String> openingHours;

     public UpdateMyStoreCommand toCommand() {
          return UpdateMyStoreCommand.from(name, phone, address, lat, lng, description, announcement, thumbnail, breakDates, breakStart, breakEnd, adult, thumbnailUpdated, filter, menuLists, addSns, openingHours);
     }

     public void setName(String name) {
          this.name = name;
     }

     public void setPhone(String phone) {
          this.phone = phone;
     }

     public void setAddress(String address) {
          this.address = address;
     }

     public void setLat(double lat) {
          this.lat = lat;
     }

     public void setLng(double lng) {
          this.lng = lng;
     }

     public void setDescription(String description) {
          this.description = description;
     }

     public void setAnnouncement(String announcement) {
          this.announcement = announcement;
     }

     public void setThumbnail(String thumbnail) {
          this.thumbnail = thumbnail;
     }

     public void setBreakStart(int breakStart) {
          this.breakStart = breakStart;
     }

     public void setBreakDates(List<Integer> breakDates) {
          this.breakDates = breakDates;
     }

     public void setBreakEnd(int breakEnd) {
          this.breakEnd = breakEnd;
     }

     public void setAdult(boolean adult) {
          this.adult = adult;
     }

     public void setThumbnailUpdated(boolean thumbnailUpdated) {
          this.thumbnailUpdated = thumbnailUpdated;
     }

     public void setFilter(String filter) {
          this.filter = filter;
     }

     public void setMenuLists(String menuLists) {
          this.menuLists = menuLists;
     }

     public void setAddSns(List<Map<String, String>> addSns) {
          this.addSns = addSns;
     }

     public void setOpeningHours(Map<String, String> openingHours) {
          this.openingHours = openingHours;
     }
}
