package com.anseong.core.domain.model.user;

import com.anseong.core.domain.model.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usr")
public class ImageUser extends AbstractBaseEntity {

     private static final long serialVersionUID = 6211810317445936674L;

     @Id
     private long id;

     @Column(name = "nickname")
     private String nickname;

     @Column(name = "profile")
     private String profile;

     ImageUser() {}

     public UserId getId() {
          return new UserId(id);
     }

     public String getNickname() {
          return nickname;
     }

     public String getProfile() {
          return profile;
     }

     @Override
     public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + (int) (getId().value() ^ (getId().value() >>> 32));
          return result;
     }

     @Override
     public boolean equals(Object obj) {
          if (this == obj) return true;
          if (obj == null) return false;
          if (!(obj instanceof ImageUser)) return false;

          ImageUser other = (ImageUser) obj;

          return this.getId().value() != 0 && this.getId().value() == other.getId().value();
     }


     @Override
     public String toString() {
          return "ReviewUser [id=" + id + ", nickname=" + nickname + ", profile=" + profile + "]";
     }
}
