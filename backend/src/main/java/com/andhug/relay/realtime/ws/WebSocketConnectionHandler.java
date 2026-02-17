package com.andhug.relay.realtime.ws;

import com.andhug.relay.realtime.MessagePayload;
import com.andhug.relay.realtime.NotificationDirector;
import com.andhug.relay.realtime.events.RealtimeConnectionClosedEvent;
import com.andhug.relay.realtime.events.RealtimeConnectionOpenedEvent;
import com.andhug.relay.realtime.registry.ConnectionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketConnectionHandler extends TextWebSocketHandler {

    private final ConnectionRegistry connectionRegistry;
    
    private final NotificationDirector notificationDirector;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        UUID profileId = getProfileId(session);

        connectionRegistry.registerConnection(new WebSocketConnection(session), profileId);
        notificationDirector.subscribeToRooms(profileId);

        applicationEventPublisher.publishEvent(RealtimeConnectionOpenedEvent.builder()
                .profileId(profileId)
                .openedAt(Instant.now())
                .build());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        String m = (String) message.getPayload();

        MessagePayload messagePayload = new ObjectMapper().readValue(m, MessagePayload.class);

        UUID profileId = getProfileId(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

        log.info("Disconnected from {}", session.getId());

        UUID profileId = getProfileId(session);

        applicationEventPublisher.publishEvent(RealtimeConnectionClosedEvent.builder()
                .profileId(profileId)
                .closedAt(Instant.now())
                .build());
    }

    private UUID getProfileId(WebSocketSession session) {

        return (UUID) session.getAttributes().get("id");
    }
}
