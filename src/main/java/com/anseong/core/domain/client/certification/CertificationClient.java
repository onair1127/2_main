package com.anseong.core.domain.client.certification;

import com.anseong.core.domain.client.certification.request.CertificationRequest;
import com.anseong.core.domain.client.certification.response.CertificationResponse;

public interface CertificationClient {
     CertificationResponse getCertificationInfo(CertificationRequest request);
}
