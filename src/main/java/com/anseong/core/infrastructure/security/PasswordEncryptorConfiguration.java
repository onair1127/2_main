package com.anseong.core.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class PasswordEncryptorConfiguration {

     @Bean
     PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
     }
}
