package com.anseong.core.domain.model.event;

import java.time.LocalDateTime;
import java.util.Objects;

import com.anseong.core.application.commands.event.RegisterReviewEventCommand;
import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.user.UserId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "event")
public class Event extends AbstractBaseEntity {

	private static final long serialVersionUID = -914115613624960147L;

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;

     @Column(name = "user_id")
     private long userId;

     @Column(name = "student_id")
     private String studentId;

     @Column(name = "image")
     private String image;

     @Column(name = "description")
     private String description;

     @Column(name = "created_date")
     private LocalDateTime createdDate;

     public static Event create(UserId userId, RegisterReviewEventCommand command) {
          Event event = new Event();
          event.userId = userId.value();
          event.studentId = command.studentId();
          event.image = command.image();
          event.description = command.description();
          event.createdDate = LocalDateTime.now();

          return event;
     }

     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof Event)) {
               return false;
          }
          Event event = (Event) o;
          return id == event.id
               && userId == event.userId
               && Objects.equals(studentId, event.studentId)
               && Objects.equals(image, event.image)
               && Objects.equals(description, event.description);
     }

     @Override
     public int hashCode() {
          return Objects.hash(id, userId, studentId, image, description);
     }

     @Override
     public String toString() {
          return "private";
     }
}
