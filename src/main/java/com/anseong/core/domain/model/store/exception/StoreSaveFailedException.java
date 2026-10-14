package com.anseong.core.domain.model.store.exception;

public class StoreSaveFailedException extends Exception {

     private static final long serialVersionUID = 7224069114866151255L;

     public StoreSaveFailedException(String message) {
          super(message);
     }
}
