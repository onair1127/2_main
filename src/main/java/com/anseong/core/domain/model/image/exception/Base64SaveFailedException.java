package com.anseong.core.domain.model.image.exception;

public class Base64SaveFailedException extends ImageSaveFailedException {

	private static final long serialVersionUID = -9222634689333360119L;

	public Base64SaveFailedException(String message) {
		super(message);
	}
}
