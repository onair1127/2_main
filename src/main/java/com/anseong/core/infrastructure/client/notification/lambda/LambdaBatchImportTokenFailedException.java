package com.anseong.core.infrastructure.client.notification.lambda;

import com.anseong.core.domain.exception.GlobalAnseongappException;

class LambdaBatchImportTokenFailedException extends GlobalAnseongappException {

     private static final long serialVersionUID = 7220318644823985299L;

     LambdaBatchImportTokenFailedException(String message) {
          super(message);
     }

     LambdaBatchImportTokenFailedException(String message, Throwable cause) {
          super(message, cause);
     }
}
