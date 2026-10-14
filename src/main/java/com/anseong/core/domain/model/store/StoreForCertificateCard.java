package com.anseong.core.domain.model.store;

import java.util.Objects;

import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.user.UserId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "store")
public class StoreForCertificateCard extends AbstractBaseEntity {

     private static final long serialVersionUID = 1974961208221962370L;

     @Id
     @Column(name = "user_id")
     private long userId;

     @Column(name = "store_name")
     private String storeName;

     @Column(name = "phone")
     private String phone;

     @Column(name = "address")
     private String address;


     public UserId getUserId() {
          return new UserId(userId);
     }

     public String getStoreName() {
          return this.storeName;
     }

     public String getPhone() {
          return phone;
     }

     public String getAddress() {
          return this.address;
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof StoreForCertificateCard)) {
               return false;
          }
          StoreForCertificateCard store = (StoreForCertificateCard) o;
          return userId == store.userId
                    && Objects.equals(storeName, store.storeName)
                    && Objects.equals(phone, store.phone)
                    && Objects.equals(address, store.address);

     }

     @Override
     public int hashCode() {
          return Objects.hash(userId, storeName, phone, address);
     }

     @Override
     public String toString() {
          return "{" +
               " userId='" + getUserId() + "'" +
               ", storeName='" + getStoreName() + "'" +
               ", phone='" + getPhone() + "'" +
               ", address='" + getAddress() + "'" +
               "}";
     }
}
