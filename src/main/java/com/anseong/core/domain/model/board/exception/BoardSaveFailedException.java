package com.anseong.core.domain.model.board.exception;

public class BoardSaveFailedException extends Exception {

     private static final long serialVersionUID = 2508342003270472437L;

     public BoardSaveFailedException(String message) {
          super(message);
     }
}
