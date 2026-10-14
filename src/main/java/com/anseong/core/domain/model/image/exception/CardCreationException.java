package com.anseong.core.domain.model.image.exception;

public class CardCreationException extends ImageSaveFailedException {

	private static final long serialVersionUID = -4214520784589265567L;

	public CardCreationException(String message) {
		super(message);
	}

}
