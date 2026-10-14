package com.anseong.core.infrastructure.client.convert.media.lambda;

import com.anseong.core.domain.client.convert.media.exception.ConvertMediaFailedException;

class LambdaConvertMediaFailedException extends ConvertMediaFailedException {

     private static final long serialVersionUID = 5491112706612922276L;

     public LambdaConvertMediaFailedException(String message) {
          super(String.format("Lambda로 미디어 컨버팅 실패 [%s]",
               message
          ));
     }

     public LambdaConvertMediaFailedException(String message, Throwable cause) {
          super(String.format("Lambda로 미디어 컨버팅 실패 [%s]",
               message
          ), cause);
     }
}
