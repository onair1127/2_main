package com.anseong.core.domain.model.user.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredEventHandler {

     private static final Logger log = LoggerFactory.getLogger(UserRegisteredEvent.class);

     @EventListener(UserRegisteredEvent.class)
     public void handleEvent(UserRegisteredEvent event) {
          log.debug("새로운 유저 회원가입 : `{}`, 시간 : `{}`", event.getUser().getNickname(), event.getUser().getCreatedDate());
     }

}
