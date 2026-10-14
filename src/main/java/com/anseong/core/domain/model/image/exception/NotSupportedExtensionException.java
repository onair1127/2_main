package com.anseong.core.domain.model.image.exception;

public class NotSupportedExtensionException extends ImageSaveFailedException {

     public NotSupportedExtensionException(String message) {
          super(message);
	}

	private static final long serialVersionUID = -8415488774984919094L;

}
