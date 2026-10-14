package com.anseong.core.infrastructure.client.convert.media;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.anseong.core.domain.client.convert.media.ConvertMediaClient;
import com.anseong.core.domain.client.convert.media.ConvertMediaPlatform;
import com.anseong.core.domain.client.convert.media.request.ConvertMediaRequest;
import com.anseong.core.domain.client.convert.media.response.ConvertMediaResponse;

import io.github.resilience4j.retry.annotation.Retry;

@Component
@Primary
@Retry(name = "convertMedia")
class ConvertMediaClientFactory implements ConvertMediaClient {

     private final Map<ConvertMediaPlatform, ConvertMediaClient> mediaConverterClients;
     private final ConvertMediaPlatform DEFAULT_PLATFORM = ConvertMediaPlatform.LAMBDA;

     ConvertMediaClientFactory(List<InternalConvertMediaClient> clients) {
          this.mediaConverterClients = clients.stream()
               .collect(Collectors.toMap(
                    InternalConvertMediaClient::supports,
                    Function.identity()
               ));
     }

     private ConvertMediaClient getClient(ConvertMediaPlatform platform) {
          if (platform == null)
               platform = DEFAULT_PLATFORM;

          return mediaConverterClients.get(platform);
     }

     @Override
     public ConvertMediaResponse process(ConvertMediaRequest request) {
          return getClient(request.platform()).process(request);
     }

     @Override
     public List<ConvertMediaResponse> process(List<ConvertMediaRequest> requests) {
          return getClient(requests.get(0).platform()).process(requests);
     }
}
