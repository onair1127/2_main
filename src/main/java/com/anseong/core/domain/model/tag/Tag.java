package com.anseong.core.domain.model.tag;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.anseong.core.application.commands.tag.TagCommand;
import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.image.Image;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.user.UserId;

@Entity
@Table(name = "tag")
public class Tag extends AbstractBaseEntity {

     private static final long serialVersionUID = -7960112700046134196L;

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(name = "user_id")
     private Long userId;

     @Column(name = "tag_name")
     private String tagName;

     @Column(name = "type")
     private String type;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "image_id")
     private Image image;

     Tag() {}

     private Tag(UserId userId, String tagName, String type, Image image) {
          this.userId = userId.value();
          this.tagName = tagName;
          this.type = type;
          this.image = image;
     }

     public static List<Tag> create(List<TagCommand> tagCommands, Image image, UserId userId) {
          return tagCommands.stream()
               .map(tagCommand -> new Tag(userId, tagCommand.tagName(), tagCommand.type(), image))
               .toList();
     }

     public static List<Tag> craete(List<Image> images, UserId userId, String tagName, String type) {
          return images.stream()
               .map(image -> new Tag(userId, tagName, type, image))
               .toList();
     }

     public static Tag craete(Image image, UserId userId, String tagName, String type) {
          return new Tag(userId, tagName, type, image);
     }

     public ImageId getImageId() {
          return image.getId();
     }

     public UserId getUserId() {
          return new UserId(userId);
     }

     public String getTagName() {
          return tagName;
     }

     public String getType() {
          return type;
     }

     @Override
     public int hashCode() {
          final int prime = 31;
          int result = 1;
          // result = prime * result + (int) (id ^ (id >>> 32));
          // result = prime * result + (int) (imageId ^ (imageId >>> 32));
          // result = prime * result + (int) (userId ^ (userId >>> 32));
          result = prime * result + ((id == null) ? 0 : id.hashCode());
          result = prime * result + ((userId == null) ? 0 : userId.hashCode());
          result = prime * result + ((tagName == null) ? 0 : tagName.hashCode());
          result = prime * result + ((type == null) ? 0 : type.hashCode());
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
          Tag other = (Tag) obj;
          if (id != other.id)
               return false;
          if (image.getId().value() != other.image.getId().value())
               return false;
          if (getUserId().value() != other.getUserId().value())
               return false;
          if (tagName == null) {
               if (other.tagName != null)
                    return false;
          } else if (!tagName.equals(other.tagName))
               return false;
          if (type == null) {
               if (other.type != null)
                    return false;
          } else if (!type.equals(other.type))
               return false;
          return true;
     }

     @Override
     public String toString() {
          return "Tag [id=" + id + ", name=" + tagName + ", type=" + type
                    + "]";
     }
}
