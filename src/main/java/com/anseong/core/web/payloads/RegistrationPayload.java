package com.anseong.core.web.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

import com.anseong.core.application.commands.user.RegistrationCommand;

public class RegistrationPayload {

     @NotNull
     @NotEmpty
     @Size(min = 6, max = 20, message = "아이디는 6자 이상 20자 이하이어야 합니다.")
     private String username;

     @NotNull
     @NotEmpty
     @Size(min = 2, max = 16, message = "닉네임 범위를 지켜주세요(영어: 4~16자, 한글: 2~8자)")
     private String nickname;

     @NotNull
     @NotEmpty
     @Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하이어야 합니다.")
     private String password;

     @NotNull
     @NotEmpty
     private String impUid;

     @NotNull
     private boolean adult;

     @NotNull
     private int imageSort;

     @NotNull
     private List<String> favoriteList;

     @NotNull
     private Set<String> boardLikeList;

     @NotNull
     private List<String> searchHistory;

     public RegistrationCommand toCommand() {
          return new RegistrationCommand(username, nickname, password, impUid, adult, imageSort, favoriteList, boardLikeList, searchHistory);
     }

     public void setUsername(String username) {
          this.username = username;
     }

     public void setNickname(String nickname) {
          this.nickname = nickname;
     }

     public void setPassword(String password) {
          this.password = password;
     }

     public void setImpUid(String impUid) {
          this.impUid = impUid;
     }

     public void setAdult(boolean adult) {
          this.adult = adult;
     }

     public void setImageSort(int imageSort) {
          this.imageSort = imageSort;
     }

     public void setFavoriteList(List<String> favoriteList) {
          this.favoriteList = favoriteList;
     }

     public void setBoardLikeList(Set<String> boardLikeList) {
          this.boardLikeList = boardLikeList;
     }

     public void setSearchHistory(List<String> searchHistory) {
          this.searchHistory = searchHistory;
     }
}
