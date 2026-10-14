package com.anseong.core.domain.model.image.exception;

public class ThumbnailCreationException extends ImageSaveFailedException {

	private static final long serialVersionUID = -4879795013258551113L;

	public ThumbnailCreationException(String message) {
		super(message);
	}

}
