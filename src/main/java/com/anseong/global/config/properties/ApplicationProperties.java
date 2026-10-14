// package com.anseong.config.properties;

// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotEmpty;

// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.validation.annotation.Validated;

// @Configuration
// @ConfigurationProperties(prefix = "app")
// @Validated
// public class ApplicationProperties {

//      @NotBlank
//      @NotEmpty
//      private String staticRoot;

//      @NotBlank
//      @NotEmpty
//      private String ffmpegRoot;

//      @NotBlank
//      @NotEmpty
//      private String activateStorageName;

//      @NotBlank
//      @NotEmpty
//      private String tokenSecret;

//      @NotBlank
//      @NotEmpty
//      private String convertApiSecret;

//      public String getStaticRoot() {
//           return this.staticRoot;
//      }

//      public void setStaticRoot(String staticRoot) {
//           this.staticRoot = staticRoot;
//      }

//      public String getActivateStorageName() {
//           return this.activateStorageName;
//      }

//      public void setActivateStorageName(String activateStorageName) {
//           this.activateStorageName = activateStorageName;
//      }

//      public String getTokenSecret() {
//           return this.tokenSecret;
//      }

//      public void setTokenSecret(String tokenSecret) {
//           this.tokenSecret = tokenSecret;
//      }

//      public String getConvertApiSecret() {
//           return this.convertApiSecret;
//      }

//      public void setConvertApiSecret(String convertApiSecret) {
//           this.convertApiSecret = convertApiSecret;
//      }

//      public String getFfmpegRoot() {
//           return this.ffmpegRoot;
//      }

//      public void setFfmpegRoot(String ffmpegRoot) {
//           this.ffmpegRoot = ffmpegRoot;
//      }

// }
