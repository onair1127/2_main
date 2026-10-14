package com.anseong.core.domain.model.image.exception;

public class ProfileCreationException extends ImageSaveFailedException {

	private static final long serialVersionUID = -8572269852677292371L;

	public ProfileCreationException(String message) {
		super(message);
	}

}
