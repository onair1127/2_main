package com.anseong.core.domain.model.image;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.anseong.core.application.commands.image.UpdateImageCommand;
import com.anseong.core.application.commands.image.UploadImageCommand;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;
import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.multiple_image.MultipleImage;
import com.anseong.core.domain.model.store.ImageStore;
import com.anseong.core.domain.model.tag.Tag;
import com.anseong.core.domain.model.user.ImageUser;
import com.anseong.core.domain.model.user.UserId;

@Entity
@Table(name = "image")
public class Image extends AbstractBaseEntity {

     private static final long serialVersionUID = -6665358826898405923L;

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;

     @Column(name = "title")
     private String title;

     @Column(name = "price")
     private int price;

     @Column(name = "description")
     private String description;

     @Column(name = "type")
     private String type;

     @Column(name = "job")
     private boolean job;

     @Embedded
     private ImageDetail detail;

     @Transient
     private Integer groupId;

     @Transient
     private int favorites;

     @Transient
     private long exposedCount;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "user_id")
     private ImageUser user;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "store_id")
     private ImageStore store;

     @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<Tag> tags;

     @OneToMany(mappedBy = "image", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<MultipleImage> multipleImages;

     Image() {}

     public static Image create(UploadImageCommand command, List<ConvertMediaResponse> convertMediaResponses, ImageUser imageUser, ImageStore imageStore) {
          Image image = new Image();
          image.title = command.title();
          image.price = command.price();
          image.description = command.description();
          image.type = command.type();
          image.job = command.isJob();
          image.detail = ImageDetail.create(command, convertMediaResponses);
          image.user = imageUser;
          image.store = imageStore;
          image.tags = Tag.create(command.tags(), image, imageUser.getId());
          image.multipleImages = MultipleImage.create(command, convertMediaResponses, image);

          return image;
     }

     public void update(UpdateImageCommand command) {
          this.title = command.title();
          this.job = command.job();
          this.price = command.price();
          this.description = command.description();
     }

     public ImageId getId() {
          return new ImageId(id);
     }

     public UserId getUserId() {
          return user.getId();
     }

     public UserId getStoreId() {
          return store.getId();
     }

     public String getTitle() {
          return this.title;
     }

     public int getPrice() {
          return this.price;
     }

     public String getDescription() {
          return this.description;
     }

     public String getType() {
          return type;
     }

     public boolean isJob() {
          return this.job;
     }

     public void setJob(boolean job) {
          this.job = job;
     }

     public ImageDetail getDetail() {
          return detail;
     }

     public Integer getGroupId() {
          return groupId;
     }

     public int getFavorites() {
          return this.favorites;
     }

     public long getExposedCount() {
          return exposedCount;
     }

     public ImageStore getStore() {
          return this.store;
     }

     public List<Tag> getTags() {
          return this.tags;
     }

     public List<MultipleImage> getMultipleImages() {
          return this.multipleImages;
     }

     public ImageUser getUser() {
          return user;
     }

     public void setGroupId(Integer groupId) {
          this.groupId = groupId;
     }

     public void setFavorites(int favorites) {
          this.favorites = favorites;
     }

     public void setExposedCount(long exposedCount) {
          this.exposedCount = exposedCount;
     }

     @Override
     public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + (int) (id ^ (id >>> 32));
          result = prime * result + (int) (user.getId().value() ^ (user.getId().value() >>> 32));
          result = prime * result + (int) (store.getId().value() ^ (store.getId().value() >>> 32));
          result = prime * result + ((title == null) ? 0 : title.hashCode());
          result = prime * result + price;
          result = prime * result + ((description == null) ? 0 : description.hashCode());
          result = prime * result + ((type == null) ? 0 : type.hashCode());
          result = prime * result + (job ? 1231 : 1237);
          // result = prime * result + ((detail == null) ? 0 : detail.hashCode());
          result = prime * result + favorites;
          result = prime * result + (int) (exposedCount ^ (exposedCount >>> 32));
          result = prime * result + ((store == null) ? 0 : store.hashCode());
          result = prime * result + ((tags == null) ? 0 : tags.hashCode());
          result = prime * result + ((multipleImages == null) ? 0 : multipleImages.hashCode());
          result = prime * result + ((user == null) ? 0 : user.hashCode());
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
          Image other = (Image) obj;
          if (id != other.id)
               return false;
          if (user.getId().value() != other.user.getId().value())
               return false;
          if (store.getId().value() != other.store.getId().value())
               return false;
          if (title == null) {
               if (other.title != null)
                    return false;
          } else if (!title.equals(other.title))
               return false;
          if (price != other.price)
               return false;
          if (description == null) {
               if (other.description != null)
                    return false;
          } else if (!description.equals(other.description))
               return false;
          if (type == null) {
               if (other.type != null)
                    return false;
          } else if (!type.equals(other.type))
               return false;
          if (job != other.job)
               return false;
          // if (detail == null) {
          //      if (other.detail != null)
          //           return false;
          // } else if (!detail.equals(other.detail))
          //      return false;
          if (favorites != other.favorites)
               return false;
          if (exposedCount != other.exposedCount)
               return false;
          if (store == null) {
               if (other.store != null)
                    return false;
          } else if (!store.equals(other.store))
               return false;
          if (tags == null) {
               if (other.tags != null)
                    return false;
          } else if (!tags.equals(other.tags))
               return false;
          if (multipleImages == null) {
               if (other.multipleImages != null)
                    return false;
          } else if (!multipleImages.equals(other.multipleImages))
               return false;
          if (user == null) {
               if (other.user != null)
                    return false;
          } else if (!user.equals(other.user))
               return false;
          return true;
     }

     @Override
     public String toString() {
          return "Image [id=" + id + ", userId=" + user.getId().value() + ", storeId=" + store.getId().value() + ", title=" + title + ", price="
                    + price + ", description=" + description + ", type=" + type + ", job=" + job + ", detail=" + detail
                    + ", favorites=" + favorites + ", exposedCount=" + exposedCount + ", store=" + store + ", tags="
                    + tags + ", multipleImages=" + multipleImages + "]";
     }
}
