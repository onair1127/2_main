package com.anseong.core.domain.model.store;

import java.util.Objects;
import java.util.Set;

import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.tag.StoreTag;
import com.anseong.core.domain.model.user.UserId;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "store")
public class StoreGeo extends AbstractBaseEntity {

     private static final long serialVersionUID = 1920654110509951127L;

     @Id
     @Column(name = "user_id")
     private Long userId;

     @Column(name = "store_name")
     private String storeName;

     @Column(name = "thumbnail")
     private String thumbnail;

     @Column(name = "filter")
     private String filter;

     @Column(name = "poster")
     private String poster;

     @Column(name = "lat")
     private double lat;

     @Column(name = "lng")
     private double lng;

     @Column(name = "adult")
     private boolean adult;

     @OneToMany
     @JoinColumn(name = "user_id", insertable = false, updatable = false)
     private Set<StoreTag> tags;

     @Embedded
     private OpeningHours openingHours;

     public UserId getUserId() {
          return new UserId(userId);
     }

     public String getStoreName() {
          return this.storeName;
     }

     public String getThumbnail() {
          return thumbnail;
     }

     public String getPoster() {
          return poster;
     }

     public String getFilter() {
          return filter;
     }

     public double getLat() {
          return this.lat;
     }

     public double getLng() {
          return this.lng;
     }

     public boolean isAdult() {
          return adult;
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof StoreGeo)) {
               return false;
          }
          StoreGeo storeGeo = (StoreGeo) o;
          return userId == storeGeo.userId
                    && Objects.equals(storeName, storeGeo.storeName)
                    && Objects.equals(thumbnail, storeGeo.thumbnail)
                    && Objects.equals(poster, storeGeo.poster)
                    && Objects.equals(filter, storeGeo.filter)
                    && lat == storeGeo.lat
                    && lng == storeGeo.lng
                    && adult == storeGeo.adult;
     }

     @Override
     public int hashCode() {
          return Objects.hash(userId, storeName, thumbnail, poster, filter, lat, lng, adult);
     }

     @Override
     public String toString() {
          return "{" +
               " id='" + userId + "'" +
               " storeName='" + getStoreName() + "'" +
               ", lat='" + getLat() + "'" +
               ", lng='" + getLng() + "'" +
          "}";
     }
}
