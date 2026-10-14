package com.anseong.core.domain.model.image.exception;

public class ImageDuplicatedException extends ImageSaveFailedException {

	private static final long serialVersionUID = -1776087327227769411L;

	public ImageDuplicatedException(String message) {
		super(message);
	}

}
