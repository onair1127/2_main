package com.anseong.core.domain.model.user;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.anseong.core.domain.model.user.response.UserResponse;

public class SimpleUser implements UserDetails {

     private static final long serialVersionUID = -8927583419016060688L;

     private UserId userId;
     private CeoId ceoId;
     private String username;
     private String password;
     private String nickname;
     private int birth;
     private int imageSort = 3;
     private boolean adult;
     private boolean temporary;
     private boolean notRegisteredCard;
     private boolean storeCreated;
     private String profile;
     private String logo;

     private SimpleUser() {}

     public SimpleUser(User user) {
          this.userId = user.getId();
          this.ceoId = user.getCeoId();
          this.username = user.getUsername();
          this.password = user.getPassword();
          this.nickname = user.getNickname();
          this.birth = user.getBirth();
          this.adult = user.isAdult();
          this.profile = user.getProfile();
          this.logo = user.getLogo();
          this.temporary = user.getPhone().equals("temporary");
          this.notRegisteredCard = user.getMerchantUid() == null ? true : false;
          this.storeCreated = user.getCeoId().isValid() ? user.isStoreCreated() : false;
     }

     public static SimpleUser from(UserResponse userResponse) {
          SimpleUser user = new SimpleUser();
          user.userId = userResponse.id();
          user.ceoId = userResponse.ceoId();
          user.username = userResponse.username();
          user.password = userResponse.password();
          user.nickname = userResponse.nickname();
          user.birth = userResponse.birth();
          user.adult = userResponse.adult();
          user.profile = userResponse.profile();
          user.logo = userResponse.logo();
          user.temporary = userResponse.phone().equals("temporary");
          user.notRegisteredCard = userResponse.merchantUid() == null ? true : false;
          user.storeCreated = userResponse.ceoId().isValid() ? userResponse.storeCreated() : false;

          return user;
     }

     public static SimpleUser create(UserId userId, CeoId ceoId, String username, String nickname, int birth, int imageSort, boolean adult, boolean temporary, boolean notRegisteredCard, boolean storeCreated, String profile, String logo) {
          SimpleUser user = new SimpleUser();
          user.userId = userId;
          user.ceoId = ceoId;
          user.username = username;
          user.nickname = nickname;
          user.birth = birth;
          user.imageSort = imageSort;
          user.temporary = temporary;
          user.notRegisteredCard = notRegisteredCard;
          user.adult = adult;
          user.storeCreated = storeCreated;
          user.profile = profile;
          user.logo = logo;

          return user;
     }

     public UserId getUserId() {
          return this.userId;
     }

     public CeoId getCeoId() {
          return this.ceoId;
     }

     @Override
	public String getUsername() {
		return this.username;
	}

     @Override
     public String getPassword() {
          return this.password;
     }

     public void setNickname(String nickname) {
          this.nickname = nickname;
     }

     public String getNickname() {
          return nickname;
     }

     public int getBirth() {
          return birth;
     }

     public void setAdult(boolean adult) {
          this.adult = adult;
     }

     public boolean isAdult() {
          return adult;
     }

     public void setTemporary(boolean temporary) {
          this.temporary = temporary;
     }

     public boolean isTemporary() {
          return temporary;
     }

     public void setNotRegisteredCard(boolean notRegisteredCard) {
          this.notRegisteredCard = notRegisteredCard;
     }

     public boolean isNotRegisteredCard() {
          return notRegisteredCard;
     }

     public void setStoreCreated(boolean storeCreated) {
          this.storeCreated = storeCreated;
     }

     public boolean isStoreCreated() {
          return this.storeCreated;
     }

     public String getProfile() {
          return profile;
     }

     public String getLogo() {
          return logo;
     }

     public void setProfile(String profile) {
          this.profile = profile;
     }

     public void setLogo(String logo) {
          this.logo = logo;
     }

     public int getImageSort() {
          return imageSort;
     }

     public void setImageSort(int imageSort) {
          this.imageSort = imageSort;
     }

	@Override
     public Collection<? extends GrantedAuthority> getAuthorities() {
          if (this.ceoId.isValid()) {
               return Collections.singleton(new SimpleGrantedAuthority("ROLE_CEO"));
          } else {
               return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
          }
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
     public boolean isEnabled() {
          return true;
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof SimpleUser)) {
               return false;
          }
          SimpleUser simpleUser = (SimpleUser) o;
          return Objects.equals(userId, simpleUser.userId)
                    && Objects.equals(ceoId, simpleUser.ceoId)
                    && Objects.equals(username, simpleUser.username)
                    && Objects.equals(nickname, simpleUser.nickname)
                    && Objects.equals(profile, simpleUser.profile)
                    && Objects.equals(logo, simpleUser.logo)
                    && birth == simpleUser.birth
                    && imageSort == simpleUser.imageSort
                    && temporary == simpleUser.temporary
                    && notRegisteredCard == simpleUser.notRegisteredCard
                    && adult == simpleUser.adult
                    && storeCreated == simpleUser.storeCreated;
     }

     @Override
     public int hashCode() {
          return Objects.hash(userId, ceoId, username, birth, imageSort, temporary, notRegisteredCard, storeCreated, profile, logo);
     }

     @Override
     public String toString() {
          return
          "{" +
               // " userId='" + getUserId() + "'" +
               // ", ceoId='" + getCeoId() + "'" +
               "username='" + getUsername() + "'" +
               // ", password='" + getPassword() + "'" +
          "}";
     }
}
