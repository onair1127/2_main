package com.anseong.core.infrastructure.client.certification.iamport;

import java.io.IOException;
import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.anseong.core.domain.client.certification.CertificationPlatform;
import com.anseong.core.domain.client.certification.request.CertificationRequest;
import com.anseong.core.domain.client.certification.response.CertificationResponse;
import com.anseong.core.infrastructure.client.certification.InternalCertificationClient;
import com.anseong.core.infrastructure.client.certification.iamport.ResponseBody.Certification;
import com.anseong.global.utils.JsonUtils;

@Component
class IamportCertificationClient implements InternalCertificationClient {

     private final RestClient iamportRestClient;

     IamportCertificationClient(@Qualifier("iamportRestClient") RestClient iamportRestClient) {
          this.iamportRestClient = iamportRestClient;
     }

     @Override
     public CertificationResponse getCertificationInfo(CertificationRequest request) {
          IamportPayload<Certification> responseBody = iamportRestClient.get()
               .uri("certifications/" + request.uid())
               .retrieve()
               .onStatus(HttpStatusCode::isError, (req, res) -> throwError(IamportCertificationErrorType.CERTIFICATION_FAILED, res))
               .body(getTypeReference(ResponseBody.Certification.class));

          if (responseBody == null)
               throw new IamportCertificationFailedException(IamportCertificationErrorType.CERTIFICATION_FAILED, HttpStatus.ACCEPTED, "응답 바디가 비어있음.");

          Certification response = responseBody.response();
          return response.toDomainResponse();
     }

     @Override
     public CertificationPlatform support() {
          return CertificationPlatform.IAMPORT;
     }

     private <T extends IamportResponseBody> ParameterizedTypeReference<IamportPayload<T>> getTypeReference(Class<T> bodyClassType) {
          Type type = ResolvableType.forClassWithGenerics(
               IamportPayload.class,
               bodyClassType
          ).getType();

          return ParameterizedTypeReference.forType(type);
     }

     private void throwError(IamportCertificationErrorType errorType, ClientHttpResponse errorResponse) throws IOException {
          IamportErrorResponse response = JsonUtils.toObject(errorResponse.getBody(), IamportErrorResponse.class);
          String errorMessage = response != null ? response.message() : ("알 수 없는 에러 : " + errorResponse.getStatusText());

          throw new IamportCertificationFailedException(errorType, errorResponse.getStatusCode(), errorMessage);
     }
}
