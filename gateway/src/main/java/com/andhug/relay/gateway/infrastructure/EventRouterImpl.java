package com.andhug.relay.gateway.infrastructure;

import com.andhug.relay.gateway.application.EventRouter;
import com.andhug.relay.gateway.application.SessionRegistry;
import com.andhug.relay.gateway.domain.model.Session;
import com.andhug.relay.gateway.infrastructure.config.GatewayProperties;
import com.andhug.relay.shared.domain.model.InboundEvent;
import com.andhug.relay.shared.domain.model.OutboundEvent;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventRouterImpl implements EventRouter {

  /** Inbound channel name, relative to gateway i.e. application services -> gateway */
  private static final String INBOUND_CHANNEL_FORMAT = "gateway:%s:inbound";

  /** Outbound channel name, relative to gateway i.e. gateway -> application services */
  private static final String OUTBOUND_CHANNEL_FORMAT = "gateway:%s:outbound";

  private final GatewayProperties gatewayProperties;

  private final ReactiveRedisTemplate<String, String> redisTemplate;

  private final SessionRegistry sessionRegistry;

  private final ObjectMapper mapper;

  @Override
  public Mono<Void> forward(InboundEvent inboundEvent) {

    return redisTemplate
        .convertAndSend(getOutboundChannel(), inboundEvent.toString())
        .doOnSuccess(
            receivers ->
                log.info("Forwarded event {} to {} receivers", inboundEvent.toString(), receivers))
        .doOnError(
            error ->
                log.error(
                    "Failed to publish to channel '{}': {}",
                    getOutboundChannel(),
                    error.getMessage()))
        .then();
  }

  @Override
  public void receive(OutboundEvent outboundEvent) {
    log.info("Received outbound event for {}", outboundEvent.profileId().toString());
    Set<Session> sessions = sessionRegistry.getSessionsByProfileId(outboundEvent.profileId());
    log.info("Sending event to {} sessions", sessions.size());
    sessions.forEach(
        session -> {
          session.sendEvent(outboundEvent.eventType(), outboundEvent.eventData());
        });
  }

  @EventListener(ApplicationReadyEvent.class)
  public void receive() {
    redisTemplate
        .listenToChannel(getInboundChannel())
        .map(event -> event.getMessage())
        .map(rawEvent -> mapper.readValue(rawEvent, OutboundEvent.class))
        .subscribe(this::receive);
    log.info("Now listening for events on Redis Pub/Sub channel {}", getInboundChannel());
  }

  private String getOutboundChannel() {
    return OUTBOUND_CHANNEL_FORMAT.formatted(gatewayProperties.getGatewayId().toString());
  }

  private String getInboundChannel() {
    return INBOUND_CHANNEL_FORMAT.formatted(gatewayProperties.getGatewayId().toString());
  }
}
