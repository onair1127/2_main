package com.anseong.core.infrastructure.client.certification;

import com.anseong.core.domain.client.certification.CertificationClient;
import com.anseong.core.domain.client.certification.CertificationPlatform;

public interface InternalCertificationClient extends CertificationClient {
     CertificationPlatform support();
}
