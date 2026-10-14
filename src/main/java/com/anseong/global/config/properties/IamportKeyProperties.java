// package com.anseong.config.properties;

// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotEmpty;

// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.validation.annotation.Validated;

// @Configuration
// @ConfigurationProperties(prefix = "api.iamport")
// @Validated
// public class IamportKeyProperties {

//      @NotBlank
//      @NotEmpty
//      private String key;

//      @NotBlank
//      @NotEmpty
//      private String secret;

//      public String getKey() {
//           return this.key;
//      }

//      public void setKey(String key) {
//           this.key = key;
//      }

//      public String getSecret() {
//           return this.secret;
//      }

//      public void setSecret(String secret) {
//           this.secret = secret;
//      }
// }
