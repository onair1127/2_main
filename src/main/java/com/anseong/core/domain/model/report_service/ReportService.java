package com.anseong.core.domain.model.report_service;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.anseong.core.application.commands.report.ReportServiceCommand;
import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.user.UserId;

@Entity
@Table(name = "report_service")
public class ReportService extends AbstractBaseEntity {

	private static final long serialVersionUID = -6947515613624964455L;

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;

     @Column(name = "user_id")
     private long userId;

     @Column(name = "image")
     private String image;

     @Column(name = "phone")
     private String phone;

     @Column(name = "content")
     private String content;

     @Column(name = "created_date")
     private LocalDateTime createdDate;

     public static ReportService create(UserId userId, ReportServiceCommand command) {
          ReportService reportService = new ReportService();
          reportService.userId = userId.value();
          reportService.image = command.image();
          reportService.phone = command.phone();
          reportService.content = command.content();
          reportService.createdDate = LocalDateTime.now();

          return reportService;
     }

     public long getId() {
          return this.id;
     }

     public UserId getUserId() {
          return new UserId(this.userId);
     }

     public String getImage() {
          return this.image;
     }

     public String getPhone() {
          return this.phone;
     }

     public String getContent() {
          return this.content;
     }

     public LocalDateTime getCreatedDate() {
          return this.createdDate;
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof ReportService)) {
               return false;
          }
          ReportService reportService = (ReportService) o;
          return Objects.equals(id, reportService.id) && Objects.equals(userId, reportService.userId) && Objects.equals(image, reportService.image) && Objects.equals(phone, reportService.phone) && Objects.equals(content, reportService.content) && Objects.equals(createdDate, reportService.createdDate);
     }

     @Override
     public int hashCode() {
          return Objects.hash(id, userId, image, phone, content);
     }

     @Override
     public String toString() {
          return "{" +
               " id='" + getId() + "'" +
               ", image='" + getImage() + "'" +
               ", phone='" + getPhone() + "'" +
               ", description='" + getContent() + "'" +
               ", createdDate='" + getCreatedDate() + "'" +
               "}";
     }
}
