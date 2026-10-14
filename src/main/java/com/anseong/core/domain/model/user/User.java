package com.anseong.core.domain.model.user;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import com.anseong.core.domain.model.AbstractBaseEntity;

@Entity
@Table(name = "usr")
public class User extends AbstractBaseEntity {

     private static final long serialVersionUID = -9215364436509723970L;

	@Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(name = "username")
     private String username;

     @Column(name = "nickname")
     private String nickname;

     @Column(name = "ceo_id")
     private UUID ceoId;

     @Column(name = "password")
     private String password;

     @CreatedDate
     @Column(name = "created_date")
     private LocalDateTime createdDate;

     @Column(name = "customer_uid")
     private String customerUid;

     @Column(name = "merchant_uid")
     private String merchantUid;

     @Column(name = "phone")
     private String phone;

     @Column(name = "adult")
     private boolean adult;

     @Column(name = "archived")
     private boolean archived;

     @Column(name = "store_created")
     private boolean storeCreated;

     @Column(name = "birth")
     private int birth;

     @Column(name = "profile")
     private String profile;

     @Column(name = "logo")
     private String logo;

     User() {}

     public static User fromId(UserId userId) {
          User user = new User();
          user.id = userId.value();

          return user;
     }

     public static User create(String username, String nickname, String password, String phone, boolean adult, int birth) {
          User user = new User();
          user.username = username;
          user.nickname = nickname;
          user.password = password;
          user.phone = phone;
          user.adult = adult;
          user.ceoId = null;
          user.createdDate = LocalDateTime.now();
          user.storeCreated = false;
          user.birth = birth;

          return user;
     }

     public static User createCEO(String username, String nickname, String password, String phone, int birth) {
          User ceo = new User();
          ceo.username = username;
          ceo.nickname = nickname;
          ceo.password = password;
          ceo.phone = phone;
          ceo.adult = true;
          ceo.ceoId = UUID.randomUUID();
          ceo.createdDate = LocalDateTime.now();
          ceo.storeCreated = false;
          ceo.birth = birth;

          return ceo;
     }

     public static User createCEOByAdmin(String username, String nickname, String password) {
          User ceo = new User();
          ceo.username = username;
          ceo.nickname = nickname;
          ceo.password = password;
          ceo.phone = "temporary";
          ceo.adult = true;
          ceo.ceoId = UUID.randomUUID();
          ceo.createdDate = LocalDateTime.now();
          ceo.storeCreated = false;
          ceo.birth = 0;

          return ceo;
     }

     public UserId getId() {
          return new UserId(id);
     }

     public String getUsername() {
          return this.username;
     }

     public String getNickname() {
          return this.nickname;
     }

     public void setCeoId(String ceoId) {
          this.ceoId = UUID.fromString(ceoId);
     }

     public CeoId getCeoId() {
          return new CeoId(ceoId);
     }

     public String getPassword() {
          return this.password;
     }

     public LocalDateTime getCreatedDate() {
          return this.createdDate;
     }

     public String getCustomerUid() {
          return customerUid;
     }

     public String getMerchantUid() {
          return merchantUid;
     }

     public String getPhone() {
          return phone;
     }

     public boolean isAdult() {
          return adult;
     }

     public boolean isArchived() {
          return archived;
     }

     public void setStoreCreated(boolean storeCreated) {
          this.storeCreated = storeCreated;
     }

     public boolean isStoreCreated() {
          return this.storeCreated;
     }

     public int getBirth() {
          return birth;
     }

     public String getProfile() {
          return profile;
     }

     public String getLogo() {
          return logo;
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof User)) {
               return false;
          }
          User user = (User) o;
          return id == user.id && Objects.equals(username, user.username) && Objects.equals(nickname, user.nickname)
                    && Objects.equals(ceoId, user.ceoId) && Objects.equals(password, user.password)
                    && Objects.equals(createdDate, user.createdDate) && Objects.equals(merchantUid, user.merchantUid)
                    && Objects.equals(customerUid, user.customerUid) && Objects.equals(phone, user.phone)
                    && Objects.equals(profile, user.profile) && Objects.equals(logo, user.logo)
                    && Objects.equals(adult, user.adult)
                    && archived == user.archived
                    && storeCreated == user.storeCreated
                    && birth == user.birth;

     }

     @Override
     public int hashCode() {
          return Objects.hash(id, username, nickname, ceoId, password, createdDate, customerUid, merchantUid, phone, adult, archived, storeCreated, birth, profile, logo);
     }

     @Override
     public String toString() {
          return "{" +
               " id='" + getId() + "'" +
               ", username='" + getUsername() + "'" +
               ", nickname='" + getNickname() + "'" +
               // ", ceoId='" + getCeoId() + "'" +
               // ", password='" + getPassword() + "'" +
               ", createdDate='" + getCreatedDate() + "'" +
               "}";
     }
}
