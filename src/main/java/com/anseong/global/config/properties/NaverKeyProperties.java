// package com.anseong.config.properties;

// import jakarta.validation.constraints.NotEmpty;
// import jakarta.validation.constraints.NotNull;

// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.validation.annotation.Validated;

// @Configuration
// @ConfigurationProperties(prefix = "api.naver")
// @Validated
// public class NaverKeyProperties {

//      @NotNull
//      @NotEmpty
//      private String clientId;

//      @NotNull
//      @NotEmpty
//      private String clientSecret;

//      @NotNull
//      @NotEmpty
//      private String accessKey;

//      @NotNull
//      @NotEmpty
//      private String secretKey;

//      @NotNull
//      @NotEmpty
//      private String alimTalkId;

//      @NotNull
//      @NotEmpty
//      private String smsId;

//      public String getClientId() {
//           return this.clientId;
//      }

//      public void setClientId(String clientId) {
//           this.clientId = clientId;
//      }

//      public String getClientSecret() {
//           return this.clientSecret;
//      }

//      public void setClientSecret(String clientSecret) {
//           this.clientSecret = clientSecret;
//      }

//      public String getAccessKey() {
//           return accessKey;
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

//      public String getAlimTalkId() {
//           return this.alimTalkId;
//      }

//      public void setAlimTalkId(String alimTalkId) {
//           this.alimTalkId = alimTalkId;
//      }

//      public String getSmsId() {
//           return this.smsId;
//      }

//      public void setSmsId(String smsId) {
//           this.smsId = smsId;
//      }
// }
