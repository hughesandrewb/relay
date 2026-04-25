package com.andhug.relay.gateway.infrastructure.ws;

import com.andhug.relay.gateway.application.RealtimeConnectionHandler;
import com.andhug.relay.gateway.application.SessionRegistry;
import com.andhug.relay.gateway.domain.model.Session;
import com.andhug.relay.gateway.domain.model.SessionId;
import com.andhug.relay.gateway.infrastructure.config.GatewayProperties;
import com.andhug.relay.realtime.domain.repository.PresenceRepository;
import com.andhug.relay.shared.domain.model.InboundEvent;
import com.andhug.relay.shared.domain.model.ProfileId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
public class GatewayWebSocketHandler extends RealtimeConnectionHandler implements WebSocketHandler {

  private final JsonMapper jsonMapper;

  public GatewayWebSocketHandler(
      GatewayProperties gatewayProperties,
      SessionRegistry sessionRegistry,
      PresenceRepository presenceRepository,
      JsonMapper jsonMapper) {
    super(gatewayProperties, sessionRegistry, presenceRepository);
    this.jsonMapper = jsonMapper;
  }

  @Override
  public Mono<Void> handle(WebSocketSession session) {

    ProfileId profileId = (ProfileId) session.getAttributes().get("profileId");
    SessionId sessionId = SessionId.of(session.getId());

    Session gatewaySession = this.handleConnection(profileId, sessionId);

    Mono<Void> incomingMessages =
        session
            .receive()
            .map(WebSocketMessage::getPayloadAsText)
            .map(rawEvent -> jsonMapper.readValue(rawEvent, InboundEvent.class))
            .flatMap(inboundEvent -> handleInboundEvent(gatewaySession, inboundEvent))
            .then();

    Mono<Void> outgoingMessages =
        session
            .send(
                gatewaySession
                    .getMessageSink()
                    .asFlux()
                    .map(e -> session.textMessage(jsonMapper.writeValueAsString(e))))
            .doOnError(
                error ->
                    log.error(
                        "Error sending message to session {}: {}",
                        session.getId(),
                        error.getMessage()));

    return Mono.when(incomingMessages, outgoingMessages);
  }
}
