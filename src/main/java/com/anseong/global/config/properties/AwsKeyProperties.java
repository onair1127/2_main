// package com.anseong.config.properties;

// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotEmpty;

// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.validation.annotation.Validated;

// @Configuration
// @ConfigurationProperties(prefix = "api.aws")
// @Validated
// public class AwsKeyProperties {

//      @NotBlank
//      @NotEmpty
//      private String accessKey;

//      @NotBlank
//      @NotEmpty
//      private String secretKey;

//      public String getAccessKey() {
//           return this.accessKey;
//      }

//      public void setAccessKey(String accessKey) {
//           this.accessKey = accessKey;
//      }

//      public String getSecretKey() {
//           return this.secretKey;
//      }

//      public void setSecretKey(String secretKey) {
//           this.secretKey = secretKey;
//      }
// }
