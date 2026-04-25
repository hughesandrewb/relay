package com.andhug.relay.gateway.application;

import com.andhug.relay.gateway.domain.model.Opcode;
import com.andhug.relay.gateway.domain.model.Session;
import com.andhug.relay.gateway.domain.model.SessionId;
import com.andhug.relay.gateway.infrastructure.config.GatewayProperties;
import com.andhug.relay.realtime.domain.repository.PresenceRepository;
import com.andhug.relay.shared.domain.exception.InvalidArgumentException;
import com.andhug.relay.shared.domain.model.CustomStatus;
import com.andhug.relay.shared.domain.model.InboundEvent;
import com.andhug.relay.shared.domain.model.PresenceStatus;
import com.andhug.relay.shared.domain.model.ProfileId;
import com.andhug.relay.shared.domain.model.ProfilePresence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;

@Slf4j
@RequiredArgsConstructor
public class RealtimeConnectionHandler {

  private final GatewayProperties gatewayProperties;

  private final SessionRegistry sessionRegistry;

  private final PresenceRepository presenceRepository;

  protected Session handleConnection(ProfileId profileId, SessionId sessionId) {

    ProfilePresence profilePresence = presenceRepository.findByProfileId(profileId);

    if (profilePresence == null) {
      profilePresence = ProfilePresence.of(PresenceStatus.ONLINE, CustomStatus.of("Online"));
    }

    profilePresence.addGateway(gatewayProperties.getGatewayId());

    presenceRepository.saveProfilePresence(profileId, profilePresence);

    return sessionRegistry.createSession(sessionId, profileId);
  }

  protected Mono<Void> handleInboundEvent(Session session, InboundEvent inboundEvent) {

    Opcode opcode = Opcode.fromCode(inboundEvent.opcode());
    JsonNode data = inboundEvent.data();

    switch (opcode) {
      case HEARTBEAT:
        break;
      case PRESENCE_UPDATE:
        break;
      case RESUME:
        session.replayFrom(extractSequence(data));
        break;
      case RECONNECT:
        break;
      default:
        throw new InvalidArgumentException("Invalid opcode received: " + inboundEvent.opcode());
    }

    return Mono.empty();
  }

  private int extractSequence(JsonNode data) {
    try {
      return data.get("s").asInt();
    } catch (Exception e) {
      log.error("Could not parse sequence: {}", data.toPrettyString());
      throw new RuntimeException("Could not parse sequence: " + data.toPrettyString());
    }
  }
}
