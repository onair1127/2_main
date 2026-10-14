package com.anseong.core.web.payloads;

import java.util.List;
import java.util.Map;

import com.anseong.core.application.commands.store.MakeOwnStoreCommand;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MakeOwnStorePayload {

     @NotNull
     @NotEmpty(message = "가게 이름이 비어있습니다.")
     @Size(max = 16, message = "가게 이름이 너무 깁니다.")
     private String name;

     @NotNull
     private long bizNo;

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
     @Min(value = 0, message = "브레이크 타임 시작 값이 비어있습니다.")
     private int breakStart;

     @NotNull
     @Min(value = 0, message = "브레이크 타임 끝나는 값이 비어있습니다.")
     private int breakEnd;

     @NotNull
     private boolean adult;

     @NotNull
     private List<String> addSns;

     private String filter;

     private String menuLists;

     @NotNull
     private Map<String, String> openingHours;

     public MakeOwnStoreCommand toCommand() {
          return new MakeOwnStoreCommand(name, bizNo, phone, address, lat, lng, description, announcement, thumbnail, breakDates, breakStart, breakEnd, adult, addSns, filter, menuLists, openingHours);
     }

     public void setName(String name) {
          this.name = name;
     }

     public void setBizNo(long bizNo) {
          this.bizNo = bizNo;
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

     public void setBreakDates(List<Integer> breakDates) {
          this.breakDates = breakDates;
     }

     public void setBreakStart(int breakStart) {
          this.breakStart = breakStart;
     }

     public void setBreakEnd(int breakEnd) {
          this.breakEnd = breakEnd;
     }

     public void setAdult(boolean adult) {
          this.adult = adult;
     }

     public void setAddSns(List<String> addSns) {
          this.addSns = addSns;
     }

     public void setFilter(String filter) {
          this.filter = filter;
     }

     public void setMenuLists(String menuLists) {
          this.menuLists = menuLists;
     }

     public void setOpeningHours(Map<String, String> openingHours) {
          this.openingHours = openingHours;
     }
}
