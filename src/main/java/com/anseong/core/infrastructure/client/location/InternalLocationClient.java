package com.anseong.core.infrastructure.client.location;

import com.anseong.core.domain.client.location.LocationClient;
import com.anseong.core.domain.client.location.LocationPlatform;

public interface InternalLocationClient extends LocationClient {
     LocationPlatform supports();
}
