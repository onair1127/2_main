package com.anseong.core.domain.model.report;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.anseong.core.application.commands.report.ReportImageCommand;
import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.image.Image;
import com.anseong.core.domain.model.image.ImageId;
import com.anseong.core.domain.model.user.UserId;

@Entity
@Table(name = "report")
public class ReportImage extends AbstractBaseEntity {

     private static final long serialVersionUID = -3755571189377667943L;

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @Column(name = "user_id")
     private Long userId;

     @Column(name = "image_url")
     private String imageUrl;

     @Column(name = "content")
     private String content;

     @Column(name = "solved")
     private boolean solved;

     @Column(name = "created_date")
     private LocalDateTime createdDate;

     @Column(name = "reporters")
     private String reporters;

     @OneToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "image_id", insertable = false, updatable = false)
     private Image image;

     ReportImage() {}

     public static ReportImage create(UserId userId, ReportImageCommand command, Image image) {
          ReportImage reportImage = new ReportImage();

          reportImage.userId = userId.value();
          reportImage.imageUrl = command.imageUrl();
          reportImage.content = command.content();
          reportImage.solved = false;
          reportImage.createdDate = LocalDateTime.now();
          reportImage.reporters = null;

          return reportImage;
     }

     public void updateReporters(String reporters) {
          this.reporters = reporters;
     }

     public ReportImageId getId() {
          return new ReportImageId(this.id);
     }

     public ImageId getImageId() {
          return this.image.getId();
     }

     public UserId getUserId() {
          return new UserId(this.userId);
     }

     public String getImageUrl() {
          return this.imageUrl;
     }

     public String getContent() {
          return this.content;
     }

     public boolean getSolved() {
          return this.solved;
     }

     public boolean isSolved() {
          return this.solved;
     }

     public String getReporters() {
          return reporters;
     }

     public Image getImage() {
          return this.image;
     }

     public LocalDateTime getCreatedDate() {
          return this.createdDate;
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof ReportImage)) {
               return false;
          }
          ReportImage report = (ReportImage) o;
          return id == report.id
                    && userId == report.userId
                    && Objects.equals(imageUrl, report.imageUrl)
                    && Objects.equals(content, report.content)
                    && solved == report.solved
                    && Objects.equals(image, report.image)
                    && Objects.equals(reporters, report.reporters);

     }

     @Override
     public int hashCode() {
          return Objects.hash(id, userId, imageUrl, content, solved, reporters, image);
     }

     @Override
     public String toString() {
          return "{" +
               " id='" + getId() + "'" +
               ", imageId='" + getImageId() + "'" +
               ", userId='" + getUserId() + "'" +
               ", imageUrl='" + getImageUrl() + "'" +
               ", content='" + getContent() + "'" +
               ", solved='" + isSolved() + "'" +
               "}";
     }
}
