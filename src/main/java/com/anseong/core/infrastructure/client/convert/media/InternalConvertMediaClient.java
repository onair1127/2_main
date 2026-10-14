package com.anseong.core.infrastructure.client.convert.media;

import com.anseong.core.domain.client.convert.media.ConvertMediaClient;
import com.anseong.core.domain.client.convert.media.ConvertMediaPlatform;

public interface InternalConvertMediaClient extends ConvertMediaClient {
     ConvertMediaPlatform supports();
}
