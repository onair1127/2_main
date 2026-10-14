package com.anseong.core.domain.security;

public interface PasswordEncryptor {
	String encrypt(String password);
}
