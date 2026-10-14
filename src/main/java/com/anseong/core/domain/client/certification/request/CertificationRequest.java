package com.anseong.core.domain.client.certification.request;

import com.anseong.core.domain.client.certification.CertificationPlatform;

public record CertificationRequest(
     CertificationPlatform platform
     , String uid
) {
     public static Builder builder() {
          return new Builder();
     }

     public static class Builder {
          private CertificationPlatform platform;
          private String uid;

          public Builder platform(CertificationPlatform platform) {
               this.platform = platform;
               return this;
          }

          public Builder uid(String uid) {
               this.uid = uid;
               return this;
          }

          public CertificationRequest build() {
               return new CertificationRequest(platform, uid);
          }
     }
}
