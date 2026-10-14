package com.anseong.core.domain.model.store.exception;

public class TagSaveFailedException extends StoreSaveFailedException {

	private static final long serialVersionUID = 3697359952475804085L;

	public TagSaveFailedException(String message) {
		super(message);
	}
}
