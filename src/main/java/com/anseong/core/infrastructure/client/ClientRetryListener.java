package com.anseong.core.infrastructure.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.retry.RetryListener;
import org.springframework.stereotype.Component;

import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;

@Component
public class ClientRetryListener implements RetryListener {

     private static final Logger log = LoggerFactory.getLogger("RETRY-CLIENT-LOGGER");

     @Bean
     RegistryEventConsumer<Retry> myRetryRegistryEventConsumer() {
          return new RegistryEventConsumer<Retry>() {
               @Override
               public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
                    Retry retry = entryAddedEvent.getAddedEntry();
                    String name = retry.getName();

                    retry.getEventPublisher().onRetry(event ->
                         log.warn("[Retry 발생] 이름: {} | 재시도 횟수: {} | 원인 예외: {}",
                         name, event.getNumberOfRetryAttempts(), event.getLastThrowable().getMessage())
                    );

                    retry.getEventPublisher().onSuccess(event ->
                         log.info("[Retry 성공] 이름: {} | 총 시도 횟수: {}",
                         name, event.getNumberOfRetryAttempts())
                    );

                    retry.getEventPublisher().onError(event ->
                         log.error("[Retry 최종 실패] 이름: {} | 총 시도 횟수: {} | 최종 예외: {}",
                         name, event.getNumberOfRetryAttempts(), event.getLastThrowable().getMessage())
                    );
               }

               @Override
               public void onEntryRemovedEvent(io.github.resilience4j.core.registry.EntryRemovedEvent<Retry> entryRemovedEvent) {
               }

               @Override
               public void onEntryReplacedEvent(io.github.resilience4j.core.registry.EntryReplacedEvent<Retry> entryReplacedEvent) {
               }
          };
    }
}
