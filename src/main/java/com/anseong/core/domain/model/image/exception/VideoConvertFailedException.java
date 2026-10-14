package com.anseong.core.domain.model.image.exception;

public class VideoConvertFailedException extends ImageSaveFailedException {

     private static final long serialVersionUID = 4306797149870168688L;

     public VideoConvertFailedException(String message) {
          super(message);
     }

}
