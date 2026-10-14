package com.anseong.core.infrastructure.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.anseong.core.domain.security.PasswordEncryptor;

@Component
class PasswordEncryptorDelegator implements PasswordEncryptor {

     private PasswordEncoder passwordEncoder;

     PasswordEncryptorDelegator(PasswordEncoder passwordEncoder) {
          this.passwordEncoder = passwordEncoder;
     }

	@Override
	public String encrypt(String password) {
          return passwordEncoder.encode(password);
	}
}
