package com.anseong.core.domain.model.multiple_image;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.List;

import com.anseong.core.application.commands.image.UploadImageCommand;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;
import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.image.Image;
import com.anseong.core.domain.model.image.ImageDetail;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.user.UserId;

@Entity
@Table(name = "multiple_image")
public class MultipleImage extends AbstractBaseEntity implements Comparable<MultipleImage> {

     private static final long serialVersionUID = 4492963717886956292L;

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;

     @Column(name = "user_id")
     private long userId;

     @Column(name = "sequence")
     private int sequence;

     @Embedded
     private ImageDetail detail;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "image_id")
     private Image image;

     MultipleImage () {}

     public static List<MultipleImage> create(UploadImageCommand command,  List<ConvertMediaResponse> convertMediaResponses, Image image) {
          return convertMediaResponses.stream()
               .map(e -> {
                    MultipleImage multipleImage = new MultipleImage();

                    multipleImage.userId = image.getUserId().value();
                    multipleImage.sequence = e.sequence();
                    multipleImage.detail = ImageDetail.create(command, convertMediaResponses);
                    multipleImage.image = image;

                    return multipleImage;
               })
               .toList();
     }

     public void update(int sequence, boolean isSquare, String filter) {
          this.sequence = sequence;
          this.detail.setFilter(filter);
          this.detail.setSquare(isSquare);
          this.detail.setArchived(false);
     }

     public MultipleImageId getId() {
          return new MultipleImageId(id);
     }

     public ImageId getImageId() {
          return image.getId();
     }

     public UserId getUserId() {
          return new UserId(userId);
     }

     public int getSequence() {
          return this.sequence;
     }

     public ImageDetail getDetail() {
          return detail;
     }

     @Override
     public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + (int) (id ^ (id >>> 32));
          result = prime * result + (int) (image.getId().value() ^ (image.getId().value() >>> 32));
          result = prime * result + (int) (userId ^ (userId >>> 32));
          result = prime * result + sequence;
          result = prime * result + ((detail == null) ? 0 : detail.hashCode());
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
          MultipleImage other = (MultipleImage) obj;
          if (id != other.id)
               return false;
          if (image.getId().value() != other.image.getId().value())
               return false;
          if (userId != other.userId)
               return false;
          if (sequence != other.sequence)
               return false;
          if (detail == null) {
               if (other.detail != null)
                    return false;
          } else if (!detail.equals(other.detail))
               return false;
          return true;
     }

     @Override
     public String toString() {
          return "MultipleImage [id=" + id + ", imageId=" + image.getId().value() + ", userId=" + userId + ", sequence=" + sequence
                    + ", detail=" + detail + "]";
     }

     @Override
     public int compareTo(MultipleImage o) {
          return sequence - o.getSequence();
     }
}
