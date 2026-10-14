package com.anseong.core.domain.model.user.event;

import java.util.Objects;

import org.springframework.util.Assert;

import com.anseong.core.domain.event.DomainEvent;
import com.anseong.core.domain.model.user.User;

public class UserRegisteredEvent extends DomainEvent {

     private static final long serialVersionUID = 1L;

     private User user;

     public UserRegisteredEvent(User user) {
          super(user);
          Assert.notNull(user, "파라미터 `user`는 반드시 null이 아니여야 합니다.");
          this.user = user;
     }

     public User getUser() {
          return this.user;
     }


     @Override
     public boolean equals(Object o) {
          if (o == this)
               return true;
          if (!(o instanceof UserRegisteredEvent)) {
               return false;
          }
          UserRegisteredEvent userRegisteredEvent = (UserRegisteredEvent) o;
          return Objects.equals(user, userRegisteredEvent.user);
     }

     @Override
     public int hashCode() {
          return Objects.hashCode(user);
     }


     @Override
     public String toString() {
          return "{" +
               " user='" + getUser() + "'" +
               "}";
     }


}
