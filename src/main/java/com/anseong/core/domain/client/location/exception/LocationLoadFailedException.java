package com.anseong.core.domain.client.location.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;

public class LocationLoadFailedException extends GlobalAnseongappException {

     private static final long serialVersionUID = -2718317326065497045L;

     public LocationLoadFailedException(String message) {
          super(message);
     }

     public LocationLoadFailedException(String message, Throwable cause) {
          super(message, cause);
     }
}
