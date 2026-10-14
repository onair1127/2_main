package com.anseong.core.domain.model.sns;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.anseong.core.application.commands.sns.SnsCommand;
import com.anseong.core.domain.model.AbstractBaseEntity;
import com.anseong.core.domain.model.user.UserId;

@Entity
@Table(name = "sns")
public class Sns extends AbstractBaseEntity {

     private static final long serialVersionUID = 6746040427140584039L;

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;

     @Column(name = "user_id")
     private long userId;

     @Column(name = "type")
     private String type;

     @Column(name = "url")
     private String url;

     Sns() {}

     public static Sns create(UserId userId, SnsCommand snsCommand) {
          Sns sns = new Sns();

          sns.userId = userId.value();
          sns.type = snsCommand.type();
          sns.url = snsCommand.url();

          return sns;
     }

     public static List<Sns> create(UserId userId, List<SnsCommand> snsCommands) {
          return snsCommands.stream()
               .map(e -> {
                    Sns sns = new Sns();

                    sns.userId = userId.value();
                    sns.type = e.type();
                    sns.url = e.url();

                    return sns;
               })
               .toList();
     }

     public UserId getUserId() {
          return new UserId(userId);
     }

     public String getType() {
          return type;
     }

     public String getUrl() {
          return url;
     }

     @Override
     public int hashCode() {
          final int prime = 31;
          int result = 1;
          result = prime * result + (int) (id ^ (id >>> 32));
          result = prime * result + (int) (getUserId().value() ^ (getUserId().value() >>> 32));
          result = prime * result + ((type == null) ? 0 : type.hashCode());
          result = prime * result + ((url == null) ? 0 : url.hashCode());
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
          Sns other = (Sns) obj;
          if (id != other.id)
               return false;
          if (getUserId().value() != other.getUserId().value())
               return false;
          if (type == null) {
               if (other.type != null)
                    return false;
          } else if (!type.equals(other.type))
               return false;
          if (url == null) {
               if (other.url != null)
                    return false;
          } else if (!url.equals(other.url))
               return false;
          return true;
     }

     @Override
     public String toString() {
          return "Sns [id=" + id + ", userId=" + getUserId().value() + ", type=" + type + ", url=" + url + "]";
     }
}
