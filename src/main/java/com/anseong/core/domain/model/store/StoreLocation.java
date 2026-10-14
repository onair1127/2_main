package com.anseong.core.domain.model.store;

import java.util.Objects;

import com.anseong.core.domain.model.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "store")
public class StoreLocation extends AbstractBaseEntity {

     private static final long serialVersionUID = 4963199266863843817L;

     @Id
     @Column(name = "user_id")
     private long userId;

     @Column(name = "store_name")
     private String storeName;

     @Column(name = "address")
     private String address;

     @Column(name = "lat")
     private double lat;

     @Column(name = "lng")
     private double lng;

     public long getUserId() {
          return this.userId;
     }

     public String getStoreName() {
          return this.storeName;
     }

     public String getAddress() {
          return this.address;
     }

     public double getLat() {
          return this.lat;
     }

     public double getLng() {
          return this.lng;
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof StoreLocation)) {
               return false;
          }
          StoreLocation storeLocation = (StoreLocation) o;
          return userId == storeLocation.userId && Objects.equals(storeName, storeLocation.storeName) && Objects.equals(address, storeLocation.address) && lat == storeLocation.lat && lng == storeLocation.lng;
     }

     @Override
     public int hashCode() {
          return Objects.hash(userId, storeName, address, lat, lng);
     }

     @Override
     public String toString() {
          return "{" +
               " userId='" + getUserId() + "'" +
               ", storeName='" + getStoreName() + "'" +
               ", address='" + getAddress() + "'" +
               ", lat='" + getLat() + "'" +
               ", lng='" + getLng() + "'" +
               "}";
     }
}
