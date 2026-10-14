package com.anseong.core.domain.client.convert.media.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;

public class ExtensionNotSupportedException extends GlobalAnseongappException {

     private static final long serialVersionUID = 2054437793231531889L;

     public ExtensionNotSupportedException(String message) {
          super(message);
     }

     public ExtensionNotSupportedException(String message, Throwable cause) {
          super(message, cause);
     }
}
