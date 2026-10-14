package com.anseong.core.domain.model.store;

import java.util.Objects;
import java.util.Set;

import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.tag.StoreTag;
import com.anseong.core.domain.model.user.UserId;

import java.util.Map;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "store")
public class SearchStore extends AbstractBaseEntity {

     private static final long serialVersionUID = -6308192595728607952L;

     @Id
     @Column(name = "user_id")
     private long userId;

     @Column(name = "store_name")
     private String storeName;

     @Column(name = "address")
     private String address;

     @Column(name = "thumbnail")
     private String thumbnail;

     @Column(name = "poster")
     private String poster;

     @Column(name = "break_dates")
     private List<Integer> breakDates;

     @Column(name = "break_start")
     private int breakStart;

     @Column(name = "break_end")
     private int breakEnd;

     @Column(name = "adult")
     private boolean adult;

     @Embedded
     private OpeningHours openingHours;

     @OneToMany
     @JoinColumn(name = "user_id", insertable = false, updatable = false)
     private Set<StoreTag> tags;

     public UserId getUserId() {
          return new UserId(userId);
     }

     public String getStoreName() {
          return storeName;
     }

     public String getAddress() {
          return address;
     }

     public String getThumbnail() {
          return thumbnail;
     }

     public String getPoster() {
          return poster;
     }

     public List<Integer> getBreakDates() {
          return breakDates;
     }

     public int getBreakStart() {
          return breakStart;
     }

     public int getBreakEnd() {
          return breakEnd;
     }

     public boolean isAdult() {
          return adult;
     }

     public Set<StoreTag> getTags() {
          return tags;
     }

     public Map<String, String> getOpeningHours() {
          return openingHours.getAllOpeningHours();
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof Store)) {
               return false;
          }
          SearchStore store = (SearchStore) o;
          return userId == store.userId && Objects.equals(storeName, store.storeName)
                    && Objects.equals(address, store.address)
                    && Objects.equals(thumbnail, store.thumbnail)
                    && Objects.equals(poster, store.poster)
                    && Objects.equals(breakDates, store.breakDates)
                    && Objects.equals(tags, store.tags)
                    && breakStart == store.breakStart
                    && breakEnd == store.breakEnd
                    && adult == store.adult;

     }

     @Override
     public int hashCode() {
          return Objects.hash(userId, storeName, address, thumbnail, poster, breakDates, tags, breakStart, breakEnd, adult);
     }

     @Override
     public String toString() {
          return "{" +
               " userId='" + getUserId() + "'" +
               ", storeName='" + getStoreName() + "'" +
               ", address='" + getAddress() + "'" +
               ", thumbnail='" + getThumbnail() + "'" +
               // ", images='" + getImages() + "'" +
               "}";
     }
}
