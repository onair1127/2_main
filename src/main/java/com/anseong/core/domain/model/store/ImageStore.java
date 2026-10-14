package com.anseong.core.domain.model.store;

import java.util.Objects;
import java.util.Set;

import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.sns.Sns;
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
public class ImageStore extends AbstractBaseEntity {

     private static final long serialVersionUID = 4624161656127578793L;

     @Id
     @Column(name = "user_id")
     private long id;

     @Column(name = "store_name")
     private String storeName;

     @Column(name = "address")
     private String address;

     @Column(name = "store_description")
     private String description;

     @Column(name = "thumbnail")
     private String thumbnail;

     @Column(name = "adult")
     private boolean adult;

     @Column(name = "poster")
     private String poster;

     @Column(name = "filter")
     private String filter;

     @Embedded
     private OpeningHours openingHours;

     @OneToMany
     @JoinColumn(name = "user_id", insertable = false, updatable = false)
     private Set<Sns> sns;

     ImageStore() {}

     public UserId getId() {
          return new UserId(id);
     }

     public String getStoreName() {
          return this.storeName;
     }

     public String getAddress() {
          return this.address;
     }

     public String getDescription() {
          return this.description;
     }

     public String getThumbnail() {
          return this.thumbnail;
     }

     public String getPoster() {
          return poster;
     }

     public String getFilter() {
          return filter;
     }

     public OpeningHours getOpeningHours() {
          return openingHours;
     }

     public Set<Sns> getSns() {
          return sns;
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof ImageStore)) {
               return false;
          }
          ImageStore store = (ImageStore) o;
          return id == store.id && Objects.equals(storeName, store.storeName)
                    && Objects.equals(address, store.address)
                    && Objects.equals(description, store.description)
                    && Objects.equals(thumbnail, store.thumbnail)
                    && adult == store.adult
                    && Objects.equals(poster, store.poster)
                    && Objects.equals(filter, store.filter)
                    && Objects.equals(openingHours, store.openingHours)
                    && Objects.equals(sns, store.sns);
     }

     @Override
     public int hashCode() {
          return Objects.hash(id, storeName, address, description, thumbnail, adult, poster, filter, openingHours, sns);
     }

     @Override
     public String toString() {
          return "{" +
               " userId='" + getId().value() + "'" +
               ", storeName='" + getStoreName() + "'" +
               ", address='" + getAddress() + "'" +
               ", description='" + getDescription() + "'" +
               // ", images='" + getImages() + "'" +
          "}";
     }

     public boolean isAdult() {
          return adult;
     }
}
