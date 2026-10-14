package com.anseong.core.domain.model.user.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;

public class EventAlreadyRegisteredException extends GlobalAnseongappException {

     private static final long serialVersionUID = 4177834848639589135L;

     public EventAlreadyRegisteredException(String message) {
          super(message);
     }

}
