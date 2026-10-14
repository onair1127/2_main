package com.anseong.core.domain.model.board_store;

import java.util.ArrayList;
import java.util.List;

import com.anseong.core.application.commands.board.BoardStoreCommand;
import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.board.Board;
import com.anseong.core.domain.model.board.BoardId;
import com.anseong.core.domain.model.user.UserId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "board_store")
public class BoardStore extends AbstractBaseEntity {

     private static final long serialVersionUID = -2650272431599586951L;

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(name = "user_id")
     private Long userId;

     @Column(name = "store_name")
     private String storeName;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "board_id")
     private Board board;

     BoardStore() {}

     public static List<BoardStore> create(Board board, List<BoardStoreCommand> stores) {
          List<BoardStore> boardStores = new ArrayList<>();

          for (BoardStoreCommand command : stores) {
               BoardStore boardStore = new BoardStore();
               boardStore.userId = command.userId().value();
               boardStore.storeName = command.storeName();
               boardStore.board = board;

               boardStores.add(boardStore);
          }

          return boardStores;
     }

     public static BoardStore create(Board board, UserId userId, String storeName) {
          BoardStore boardStore = new BoardStore();
          boardStore.userId = userId.value();
          boardStore.storeName = storeName;
          boardStore.board = board;

          return boardStore;
     }

     public BoardId getBoardId() {
          return board.getId();
     }

     public UserId getUserId() {
          return new UserId(this.userId);
     }

     public String getStoreName() {
          return storeName;
     }

     @Override
     public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + (int) (id ^ (id >>> 32));
          result = prime * result + (int) (userId ^ (userId >>> 32));
          result = prime * result + ((storeName == null) ? 0 : storeName.hashCode());
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
          BoardStore other = (BoardStore) obj;
          if (id != other.id)
               return false;
          if (userId != other.userId)
               return false;
          if (storeName == null) {
               if (other.storeName != null)
                    return false;
          } else if (!storeName.equals(other.storeName))
               return false;
          return true;
     }

     @Override
     public String toString() {
          return "BoardStore [id=" + id + ", boardId=" + board.getId().value() + ", userId=" + userId + ", storeName=" + storeName
                    + "]";
     }
}
