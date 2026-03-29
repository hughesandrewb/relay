package com.andhug.relay.realtime.infrastructure.ws;

import com.andhug.relay.realtime.application.service.NotificationDirector;
import com.andhug.relay.realtime.domain.ConnectionRegistry;
import com.andhug.relay.realtime.domain.event.RealtimeConnectionClosedEvent;
import com.andhug.relay.realtime.domain.event.RealtimeConnectionOpenedEvent;
import com.andhug.relay.shared.domain.model.ProfileId;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketConnectionHandler extends TextWebSocketHandler {

  private final ConnectionRegistry connectionRegistry;

  private final NotificationDirector notificationDirector;

  private final ApplicationEventPublisher eventPublisher;

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    ProfileId profileId = getProfileId(session);

    connectionRegistry.registerConnection(new WebSocketConnection(session), profileId);
    notificationDirector.subscribeToRooms(profileId.value());

    eventPublisher.publishEvent(
        RealtimeConnectionOpenedEvent.builder()
            .profileId(profileId)
            .openedAt(Instant.now())
            .build());
  }

  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
      throws Exception {

    // String m = (String) message.getPayload();

    // MessagePayload messagePayload = new ObjectMapper().readValue(m, MessagePayload.class);

    // UUID profileId = getProfileId(session);
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception)
      throws Exception {}

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
      throws Exception {

    log.info("Disconnected from {}", session.getId());

    ProfileId profileId = getProfileId(session);

    eventPublisher.publishEvent(
        RealtimeConnectionClosedEvent.builder()
            .profileId(profileId)
            .closedAt(Instant.now())
            .build());
  }

  private ProfileId getProfileId(WebSocketSession session) {

    return (ProfileId) session.getAttributes().get("id");
  }
}
