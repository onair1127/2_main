package com.anseong.core.infrastructure.client.certification;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.anseong.core.domain.client.certification.CertificationClient;
import com.anseong.core.domain.client.certification.CertificationPlatform;
import com.anseong.core.domain.client.certification.exception.CertificationFailedException;
import com.anseong.core.domain.client.certification.request.CertificationRequest;
import com.anseong.core.domain.client.certification.response.CertificationResponse;

import io.github.resilience4j.retry.annotation.Retry;

@Component
@Primary
@Retry(name = "certification")
class CertificationClientFactory implements CertificationClient {

     private final Map<CertificationPlatform, InternalCertificationClient> clients;
     private final CertificationPlatform DEFAULT_PLATFORM = CertificationPlatform.IAMPORT;

     CertificationClientFactory(List<InternalCertificationClient> certificationClients) {
          this.clients = certificationClients.stream()
               .collect(Collectors.toMap(
                    InternalCertificationClient::support,
                    Function.identity()
               ));
     }

     private CertificationClient getClient(CertificationPlatform platform) throws CertificationFailedException {
          if (platform == null)
               platform = DEFAULT_PLATFORM;

          return clients.get(platform);
     }

     @Override
     public CertificationResponse getCertificationInfo(CertificationRequest request) throws CertificationFailedException {
          return getClient(request.platform()).getCertificationInfo(request);
     }
}
