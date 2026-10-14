package com.anseong.core.domain.client.convert.media;

import java.util.List;

import com.anseong.core.domain.client.convert.media.request.ConvertMediaRequest;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;

public interface ConvertMediaClient {
     ConvertMediaResponse process(ConvertMediaRequest request);

     List<ConvertMediaResponse> process(List<ConvertMediaRequest> requests);
}
