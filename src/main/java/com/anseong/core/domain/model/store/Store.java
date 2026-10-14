package com.anseong.core.domain.model.store;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.anseong.core.application.commands.store.MakeOwnStoreCommand;
import com.anseong.core.application.commands.store.UpdateMyStoreCommand;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;
import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.sns.Sns;
import com.anseong.core.domain.model.tag.StoreTag;
import com.anseong.core.domain.model.user.UserId;

import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "store")
public class Store extends AbstractBaseEntity {

     private static final long serialVersionUID = 6534389851471130091L;

     @Id
     @Column(name = "user_id")
     private Long userId;

     @Column(name = "store_name")
     private String storeName;

     @Column(name = "biz_no")
     private Long bizNo;

     @Column(name = "phone")
     private String phone;

     @Column(name = "address")
     private String address;

     @Column(name = "store_description")
     private String description;

     @Column(name = "announcement")
     private String announcement;

     @Column(name = "lat")
     private double lat;

     @Column(name = "lng")
     private double lng;

     @Column(name = "thumbnail")
     private String thumbnail;

     @CreatedDate
     @Column(name = "created_date")
     private LocalDateTime createdDate;

     @Column(name = "break_dates")
     private List<Integer> breakDates;

     @Column(name = "break_start")
     private int breakStart;

     @Column(name = "break_end")
     private int breakEnd;

     @Column(name = "adult")
     private boolean adult;

     @Column(name = "filter")
     private String filter;

     @Column(name = "poster")
     private String poster;

     @Column(name = "menu_lists")
     private String menuLists;

     @Embedded
     private OpeningHours openingHours;

     @OneToMany
     // @JoinColumn(name = "user_id", insertable = false, updatable = false)
     private List<Sns> sns = new ArrayList<>();

     @OneToMany
     // @JoinColumn(name = "user_id", insertable = false, updatable = false)
     private List<StoreTag> tags = new ArrayList<>();

     public static Store create(UserId userId, MakeOwnStoreCommand command, ConvertMediaResponse convertedThumbnail) {
          Store store = new Store();

          store.storeName = command.name();
          store.bizNo = command.bizNo();
          store.phone = command.phone();
          store.address = command.address();
          store.description = command.description();
          store.announcement = command.announcement();
          store.userId = userId.value();
          store.lat = command.lat();
          store.lng = command.lng();
          store.thumbnail = convertedThumbnail.poster();
          store.poster = convertedThumbnail.poster();
          store.createdDate = LocalDateTime.now();
          store.breakDates = command.breakDates();
          store.breakStart = command.breakStart();
          store.breakEnd = command.breakEnd();
          store.adult = command.adult();
          store.filter = command.filter();
          store.menuLists = command.menuLists();
          store.openingHours = new OpeningHours(command.openingHours());

          return store;
     }

     public void update(UpdateMyStoreCommand command, ConvertMediaResponse convertedThumbnail) {
          this.storeName = command.name();
          this.phone = command.phone();
          this.address = command.address();
          this.lat = command.lat();
          this.lng = command.lng();
          this.description = command.description();
          this.announcement = command.announcement();
          this.breakDates = command.breakDates();
          this.breakStart = command.breakStart();
          this.breakEnd = command.breakEnd();
          this.adult = command.adult();
          this.filter = command.filter();
          this.menuLists = command.menuLists();
          OpeningHours.update(command.openingHours(), this.openingHours);

          if (convertedThumbnail != null) {
               this.thumbnail = convertedThumbnail.thumbnail();
               this.poster = convertedThumbnail.poster();
          }
     }

     public void updateStore(String name, String phone, String address, double lat, double lng, String description, String announcement, String thumbnail,
               List<Integer> breakDates, int breakStart, int breakEnd, boolean adult, String filter, String menuLists, Map<String, String> openingHoursData) {

          this.storeName = name;
          this.phone = phone;
          this.address = address;
          this.lat = lat;
          this.lng = lng;
          this.description = description;
          this.announcement = announcement;
          this.thumbnail = thumbnail;
          this.breakDates = breakDates;
          this.breakStart = breakStart;
          this.breakEnd = breakEnd;
          this.adult = adult;
          this.filter = filter;
          this.menuLists = menuLists;
          OpeningHours.update(openingHoursData, this.openingHours);
	}

     public List<Sns> getSns() {
          return this.sns;
     }

     public List<StoreTag> getTags() {
          return this.tags;
     }

     public UserId getUserId() {
          return new UserId(userId);
     }

     public String getStoreName() {
          return this.storeName;
     }

     public long getBizNo() {
          return bizNo;
     }

     public String getPhone() {
          return phone;
     }

     public String getAddress() {
          return this.address;
     }

     public String getDescription() {
          return this.description;
     }

     public String getAnnouncement() {
          return announcement;
     }

     public double getLat() {
          return this.lat;
     }

     public double getLng() {
          return this.lng;
     }

     public String getThumbnail() {
          return this.thumbnail;
     }

     public LocalDateTime getCreatedDate() {
          return this.createdDate;
     }

     public List<Integer> getBreakDates() {
          return breakDates;
     }

     public int getBreakStart() {
          return this.breakStart;
     }

     public int getBreakEnd() {
          return this.breakEnd;
     }

     public boolean isAdult() {
          return adult;
     }

     public String getFilter() {
          return filter;
     }

     public String getPoster() {
          return poster;
     }

     public void setPoster(String poster) {
          this.poster = poster;
     }

     public String getMenuLists() {
          return menuLists;
     }

     public OpeningHours getOpeningHours() {
          return openingHours;
     }

     @Override
     public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((userId == null) ? 0 : userId.hashCode());
          result = prime * result + ((storeName == null) ? 0 : storeName.hashCode());
          result = prime * result + (int) (bizNo ^ (bizNo >>> 32));
          result = prime * result + ((phone == null) ? 0 : phone.hashCode());
          result = prime * result + ((address == null) ? 0 : address.hashCode());
          result = prime * result + ((description == null) ? 0 : description.hashCode());
          result = prime * result + ((announcement == null) ? 0 : announcement.hashCode());
          long temp;
          temp = Double.doubleToLongBits(lat);
          result = prime * result + (int) (temp ^ (temp >>> 32));
          temp = Double.doubleToLongBits(lng);
          result = prime * result + (int) (temp ^ (temp >>> 32));
          result = prime * result + ((thumbnail == null) ? 0 : thumbnail.hashCode());
          result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
          result = prime * result + ((breakDates == null) ? 0 : breakDates.hashCode());
          result = prime * result + breakStart;
          result = prime * result + breakEnd;
          result = prime * result + (adult ? 1231 : 1237);
          result = prime * result + ((filter == null) ? 0 : filter.hashCode());
          result = prime * result + ((poster == null) ? 0 : poster.hashCode());
          result = prime * result + ((menuLists == null) ? 0 : menuLists.hashCode());
          result = prime * result + ((openingHours == null) ? 0 : openingHours.hashCode());
          return result;
     }

     @Override
     public boolean equals(Object obj) {
          if (this == obj)
               return true;
          if (obj == null)
               return false;
          if (getClass() != obj.getClass())
               return false;
          Store other = (Store) obj;
          if (userId != other.userId)
               return false;
          if (storeName == null) {
               if (other.storeName != null)
                    return false;
          } else if (!storeName.equals(other.storeName))
               return false;
          if (bizNo != other.bizNo)
               return false;
          if (phone == null) {
               if (other.phone != null)
                    return false;
          } else if (!phone.equals(other.phone))
               return false;
          if (address == null) {
               if (other.address != null)
                    return false;
          } else if (!address.equals(other.address))
               return false;
          if (description == null) {
               if (other.description != null)
                    return false;
          } else if (!description.equals(other.description))
               return false;
          if (announcement == null) {
               if (other.announcement != null)
                    return false;
          } else if (!announcement.equals(other.announcement))
               return false;
          if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
               return false;
          if (Double.doubleToLongBits(lng) != Double.doubleToLongBits(other.lng))
               return false;
          if (thumbnail == null) {
               if (other.thumbnail != null)
                    return false;
          } else if (!thumbnail.equals(other.thumbnail))
               return false;
          if (createdDate == null) {
               if (other.createdDate != null)
                    return false;
          } else if (!createdDate.equals(other.createdDate))
               return false;
          if (breakDates == null) {
               if (other.breakDates != null)
                    return false;
          } else if (!breakDates.equals(other.breakDates))
               return false;
          if (breakStart != other.breakStart)
               return false;
          if (breakEnd != other.breakEnd)
               return false;
          if (adult != other.adult)
               return false;
          if (filter == null) {
               if (other.filter != null)
                    return false;
          } else if (!filter.equals(other.filter))
               return false;
          if (poster == null) {
               if (other.poster != null)
                    return false;
          } else if (!poster.equals(other.poster))
               return false;
          if (menuLists == null) {
               if (other.menuLists != null)
                    return false;
          } else if (!menuLists.equals(other.menuLists))
               return false;
          if (openingHours == null) {
               if (other.openingHours != null)
                    return false;
          } else if (!openingHours.equals(other.openingHours))
               return false;
          return true;
     }

     @Override
     public String toString() {
          return "Store [userId=" + userId + ", storeName=" + storeName + ", bizNo=" + bizNo + ", phone=" + phone
                    + ", address=" + address + ", description=" + description + ", announcement=" + announcement
                    + ", lat=" + lat + ", lng=" + lng + ", thumbnail=" + thumbnail + ", createdDate=" + createdDate
                    + ", breakDates=" + breakDates + ", breakStart=" + breakStart + ", breakEnd=" + breakEnd
                    + ", adult=" + adult + ", filter=" + filter + ", poster=" + poster + ", menuLists=" + menuLists
                    + ", openingHours=" + openingHours + ", sns=" + sns + ", tags=" + tags + "]";
     }
}
