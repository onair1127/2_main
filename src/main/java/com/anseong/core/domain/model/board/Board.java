package com.anseong.core.domain.model.board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.anseong.core.application.commands.board.UploadBoardCommand;
import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.board_store.BoardStore;
import com.anseong.core.domain.model.user.UserId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "board")
public class Board extends AbstractBaseEntity {

	private static final long serialVersionUID = 923750064993647928L;

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(name = "user_id")
     private Long userId;

     @Column(name = "thumbnail")
     private String thumbnail;

     @Column(name = "title")
     private String title;

     @Column(name = "category")
     private String category;

     @Column(name = "contents")
     private String contents;

     @Column(name = "adult")
     private boolean adult;

     @Column(name = "created_date")
     private LocalDateTime createdDate;

     @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<BoardStore> boardStores = new ArrayList<>();

     Board() {}

     public static Board create(UploadBoardCommand command, UserId userId, String selectedThumbnail, String boardContents) {
          Board board = new Board();

          board.userId = userId.value();
          board.thumbnail = selectedThumbnail;
          board.title = command.title();
          board.category = command.category();
          board.contents = boardContents;
          board.adult = command.isAdult();
          board.createdDate = LocalDateTime.now();
          board.boardStores = BoardStore.create(board, command.stores());

          return board;
     }

     public void update(UploadBoardCommand command, UserId userId, String selectedThumbnail, String boardContents) {
          this.thumbnail = selectedThumbnail;
          this.title = command.title();
          this.category = command.category();
          this.contents = boardContents;
          this.adult = command.isAdult();
     }

     public BoardId getId() {
          return new BoardId(id);
     }

     public UserId getUserId() {
          return new UserId(userId);
     }

     public String getThumbnail() {
          return this.thumbnail;
     }

     public String getTitle() {
          return this.title;
     }

     public String getCategory() {
          return this.category;
     }

     public String getContents() {
          return this.contents;
     }

     public boolean isAdult() {
          return adult;
     }

     public LocalDateTime getCreatedDate() {
          return this.createdDate;
     }

     public List<BoardStore> getBoardStores() {
          return this.boardStores;
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof Board)) {
               return false;
          }
          Board board = (Board) o;
          return id == board.id && userId == board.userId && Objects.equals(thumbnail, board.thumbnail) && Objects.equals(title, board.title) && Objects.equals(category, board.category) && Objects.equals(contents, board.contents) && Objects.equals(createdDate, board.createdDate) && adult == board.adult;
     }

     @Override
     public int hashCode() {
          return Objects.hash(id, userId, thumbnail, title, category, contents, adult, createdDate);
     }

     @Override
     public String toString() {
          return "{" +
               " id='" + getId() + "'" +
               ", userId='" + getUserId() + "'" +
               ", thumbnail='" + getThumbnail() + "'" +
               ", title='" + getTitle() + "'" +
               ", category='" + getCategory() + "'" +
               ", contents='" + getContents() + "'" +
               ", adult='" + isAdult() + "'" +
               ", createdDate='" + getCreatedDate() + "'" +
               "}";
     }
}
