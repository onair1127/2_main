package com.anseong.core.domain.model.tag;

import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.user.UserId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag")
public class StoreTag extends AbstractBaseEntity {

     private static final long serialVersionUID = -6127812700046121556L;

     @Id
     private Long id;

     @Column(name = "user_id")
     private Long userId;

     @Column(name = "tag_name")
     private String tagName;

     public UserId getUserId() {
          return new UserId(userId);
     }

     public String getTagName() {
          return tagName;
     }

     @Override
     public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + (int) (id ^ (id >>> 32));
          result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
          result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
          StoreTag other = (StoreTag) obj;
          if (id != other.id)
               return false;
          if (userId == null) {
               if (other.userId != null)
                    return false;
          } else if (!userId.equals(other.userId))
               return false;
          if (tagName == null) {
               if (other.tagName != null)
                    return false;
          } else if (!tagName.equals(other.tagName))
               return false;
          return true;
     }

     @Override
     public String toString() {
          return "StoreTag [id=" + id + ", userId=" + userId + ", tagName=" + tagName + "]";
     }
}
