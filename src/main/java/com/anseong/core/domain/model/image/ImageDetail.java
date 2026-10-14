package com.anseong.core.domain.model.image;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import com.anseong.core.application.commands.image.ImageCommand;
import com.anseong.core.application.commands.image.UploadImageCommand;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;
import com.anseong.core.domain.model.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ImageDetail extends AbstractBaseEntity {

     private static final long serialVersionUID = -6515147254185661060L;

     @Column(name = "filename")
     private String filename;

     @Column(name = "thumbnail")
     private String thumbnail;

     @Column(name = "card")
     private String card;

     @Column(name = "filter")
     private String filter;

     @Column(name = "is_square")
     private boolean isSquare;

     @Column(name = "archived")
     private boolean archived;

     @CreatedDate
     @Column(name = "created_date")
     private LocalDateTime createdDate;

     ImageDetail() {}

     public static ImageDetail create(UploadImageCommand command, List<ConvertMediaResponse> convertMediaResponses) {
          ImageCommand imageCommand = command.images().stream()
               .filter(e -> e.sequence() == 1)
               .findFirst()
               .orElseThrow();

          ConvertMediaResponse convertMediaResponse = convertMediaResponses.stream()
               .filter(e -> e.sequence() == 1)
               .findFirst()
               .orElseThrow();

          ImageDetail imageDetail = new ImageDetail();

          imageDetail.filename = imageCommand.filename();
          imageDetail.thumbnail = convertMediaResponse.thumbnail();
          imageDetail.card = convertMediaResponse.card();
          imageDetail.archived = false;
          imageDetail.filter = imageCommand.filter();
          imageDetail.isSquare = imageCommand.isSquare();
          imageDetail.createdDate = command.createdDate() != null ? command.createdDate() : LocalDateTime.now();

          return imageDetail;
     }

     public static ImageDetail create(String filename, String thumbnail, String card, String filter, boolean isSquare, boolean archived, LocalDateTime createdDate) {
          ImageDetail imageDetail = new ImageDetail();

          imageDetail.filename = filename;
          imageDetail.thumbnail = thumbnail;
          imageDetail.card = card;
          imageDetail.archived = archived;
          imageDetail.filter = filter;
          imageDetail.isSquare = isSquare;
          imageDetail.createdDate = createdDate != null ? createdDate : LocalDateTime.now();

          return imageDetail;
     }

     public String getFilename() {
          return filename;
     }

     public void setFilename(String filename) {
          this.filename = filename;
     }

     public String getThumbnail() {
          return thumbnail;
     }

     public void setThumbnail(String thumbnail) {
          this.thumbnail = thumbnail;
     }

     public String getCard() {
          return card;
     }

     public void setCard(String card) {
          this.card = card;
     }

     public boolean isArchived() {
          return archived;
     }

     public void setArchived(boolean archived) {
          this.archived = archived;
     }

     public String getFilter() {
          return filter;
     }

     public void setFilter(String filter) {
          this.filter = filter;
     }

     public boolean isSquare() {
          return isSquare;
     }

     public void setSquare(boolean isSquare) {
          this.isSquare = isSquare;
     }

     public LocalDateTime getCreatedDate() {
          return createdDate;
     }

     public void setCreatedDate(LocalDateTime createdDate) {
          this.createdDate = createdDate != null ? createdDate : LocalDateTime.now();
     }

     @Override
     public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + ((filename == null) ? 0 : filename.hashCode());
          result = prime * result + ((thumbnail == null) ? 0 : thumbnail.hashCode());
          result = prime * result + ((card == null) ? 0 : card.hashCode());
          result = prime * result + ((filter == null) ? 0 : filter.hashCode());
          result = prime * result + (isSquare ? 1231 : 1237);
          result = prime * result + (archived ? 1231 : 1237);
          result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
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
          ImageDetail other = (ImageDetail) obj;
          if (filename == null) {
               if (other.filename != null)
                    return false;
          } else if (!filename.equals(other.filename))
               return false;
          if (thumbnail == null) {
               if (other.thumbnail != null)
                    return false;
          } else if (!thumbnail.equals(other.thumbnail))
               return false;
          if (card == null) {
               if (other.card != null)
                    return false;
          } else if (!card.equals(other.card))
               return false;
          if (filter == null) {
               if (other.filter != null)
                    return false;
          } else if (!filter.equals(other.filter))
               return false;
          if (isSquare != other.isSquare)
               return false;
          if (archived != other.archived)
               return false;
          if (createdDate == null) {
               if (other.createdDate != null)
                    return false;
          } else if (!createdDate.equals(other.createdDate))
               return false;
          return true;
     }

     @Override
     public String toString() {
          return "ImageDetail [filename=" + filename + ", thumbnail=" + thumbnail + ", card=" + card + ", filter="
                    + filter + ", isSquare=" + isSquare + ", archived=" + archived + ", createdDate=" + createdDate
                    + "]";
     }
}
