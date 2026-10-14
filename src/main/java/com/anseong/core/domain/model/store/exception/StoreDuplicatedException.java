package com.anseong.core.domain.model.store.exception;

import com.anseong.core.domain.exception.GlobalAnseongappException;

/**
 * 사장님의 가게가 이미 등록될 경우 이 예외를 던집니다.
 */
public class StoreDuplicatedException extends GlobalAnseongappException {

     private static final long serialVersionUID = 6671273947864770036L;

     public StoreDuplicatedException(String message) {
          super(message);
     }
}
