package com.anseong.core.domain.client.convert.media.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;

public class ConvertMediaFailedException extends GlobalAnseongappException {

     private static final long serialVersionUID = -2206609271083351287L;

     public ConvertMediaFailedException(String message) {
          super(message);
     }

     public ConvertMediaFailedException(String message, Throwable cause) {
          super(message, cause);
     }
}
