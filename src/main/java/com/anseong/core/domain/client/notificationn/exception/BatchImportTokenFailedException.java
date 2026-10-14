package com.anseong.core.domain.client.notificationn.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;

public class BatchImportTokenFailedException extends GlobalAnseongappException {

     public BatchImportTokenFailedException(String message) {
          super(message);
     }

     public BatchImportTokenFailedException(String message, Throwable cause) {
          super(message, cause);
     }
}
