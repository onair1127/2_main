package com.anseong.core.domain.model.store;

import java.util.Objects;

import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.board_store.BoardStore;
import com.anseong.core.domain.model.user.UserId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "store")
public class SearchBoardStore extends AbstractBaseEntity {

     private static final long serialVersionUID = 5819664677252531827L;

     @Id
     @Column(name = "user_id")
     private long userId;

     @Column(name = "store_name")
     private String storeName;

     public UserId getUserId() {
          return new UserId(this.userId);
     }

     public String getStoreName() {
          return this.storeName;
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof BoardStore)) {
               return false;
          }
          SearchBoardStore boardStore = (SearchBoardStore) o;
          return userId == boardStore.userId && Objects.equals(storeName, boardStore.storeName);
     }

     @Override
     public int hashCode() {
          return Objects.hash(userId, storeName);
     }

     @Override
     public String toString() {
          return "{" +
               " userId='" + getUserId() + "'" +
               ", storeName='" + getStoreName() + "'" +
               "}";
     }
}
